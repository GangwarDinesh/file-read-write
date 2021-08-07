package com.base.app.service;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.base.app.model.ClientDetails;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class ClientServiceImpl implements ClientService {

	@Override
	public List<ClientDetails> getCsvData(MultipartFile file) {
		try {
			
			CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
			        .withSkipLines(1)
			        .build();
					
			CsvToBeanBuilder<ClientDetails> builder = new CsvToBeanBuilder<>(csvReader);
			return builder.withType(ClientDetails.class).build().parse();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Resource createCsvFile(List<ClientDetails> payload) {
		Resource resource = null;
		try {
			Path path = Paths.get("E:\\csv\\ClientDetails.csv");
			path.toFile().delete();
			FileWriter fw = new FileWriter(path.toFile());
			CSVWriter cw = new CSVWriter(fw);
			String[] header = {"id","name","email","address","company","salary"};
			cw.writeNext(header);
			payload.forEach(data->{
				String[] content = {data.getId()+"",data.getName(),data.getEmail(),data.getAddress(),data.getCompany(),data.getSalary()+""};
				cw.writeNext(content);
			});
			cw.close();
			resource = new UrlResource(path.toUri());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resource;
	}

}
