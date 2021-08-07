package com.base.app.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.base.app.model.ClientDetails;


public interface ClientService {
	public List<ClientDetails> getCsvData(MultipartFile file);
	
	public Resource createCsvFile(List<ClientDetails> payload);
}
