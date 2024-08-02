package com.namo.spring.client.social.common.utils;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.util.Base64;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import com.namo.spring.client.social.common.properties.AppleProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleUtils {
	private final AppleProperties appleProperties;

	public PrivateKey getPrivateKey() {

		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

			byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

			PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
			return converter.getPrivateKey(privateKeyInfo);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
