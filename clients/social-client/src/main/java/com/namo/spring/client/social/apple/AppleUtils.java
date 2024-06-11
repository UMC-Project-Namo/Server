package com.namo.spring.client.social.apple;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleUtils {
	private final AppleProperties appleProperties;

	public PrivateKey getPrivateKey() {
		try {
			ClassPathResource resource = new ClassPathResource(appleProperties.getPrivateKeyPath());
			String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
			Reader pemReader = new StringReader(privateKey);

			PEMParser pemParser = new PEMParser(pemReader);
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			PrivateKeyInfo object = (PrivateKeyInfo)pemParser.readObject();
			return converter.getPrivateKey(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
