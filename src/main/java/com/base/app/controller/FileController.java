package com.base.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.base.app.model.ClientDetails;
import com.base.app.model.FileUploadModel;
import com.base.app.service.ClientService;
import com.base.app.validate.FileValidator;

@RestController
@RequestMapping("/api/file")
public class FileController {
	
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Value("${spring.servlet.multipart.location}")
	private String fileBasePath;

	@Autowired
	private FileValidator fileValidator;
	
	@Autowired
	private ClientService clientService;
	
	@PostMapping("/upload")
	public ResponseEntity<String> upload(Model model, @ModelAttribute("fileUploadModel") FileUploadModel fileUploadModel, BindingResult bindingResult) {
		MultipartFile file = fileUploadModel.getFile();
		double fileSizeInKB = file.getSize()/ 1024;
		System.out.println(fileSizeInKB+"***********************************");
		fileValidator.validate(fileUploadModel, bindingResult);
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>(bindingResult.getAllErrors().get(0).getCode(),new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path path = Paths.get(fileBasePath+fileName);
		logger.info("Complete path {}",path);
		try{
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		}catch (IOException e) {
			logger.error("Exception occurred- {}",e.getMessage());
		}
		String downloadFilePath = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path("/api/file/download/")
			.path(fileName).build().encode().toUriString();
		return new ResponseEntity<>(downloadFilePath,new HttpHeaders(), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/download/{fileName}")
	public ResponseEntity download(@PathVariable("fileName") String fileName) {
		Path path = Paths.get(fileBasePath+fileName);
		Resource resource = null;
		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			logger.error("Exception occurred-# {}",e.getMessage());
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/jpg"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+resource.getFilename()+"\"").body(resource);
	}
	
	@PostMapping("/csv")
	public ResponseEntity<List<ClientDetails>> readCsvFile(@RequestParam("file") MultipartFile file){
		List<ClientDetails> clientsList = clientService.getCsvData(file);
		return new ResponseEntity<>(clientsList, new HttpHeaders(), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/write/csv")
	public ResponseEntity writeCsvFile(@RequestBody List<ClientDetails> payload){
		Resource resource = clientService.createCsvFile(payload);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"ClientDetails.csv\"").body(resource);
	}
}
