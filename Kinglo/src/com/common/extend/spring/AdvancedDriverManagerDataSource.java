package com.common.extend.spring;

import com.common.util.security.SecurityUtil;

import java.util.Properties;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public final class AdvancedDriverManagerDataSource extends
	DriverManagerDataSource {
    public AdvancedDriverManagerDataSource() {
    }

    public AdvancedDriverManagerDataSource(String url) {
	super(url);
    }

    public AdvancedDriverManagerDataSource(String url, Properties conProps) {
	super(url, conProps);
    }

    public AdvancedDriverManagerDataSource(String url, String username,
	    String password) {
	super(url, username, password);
    }

    public String getPassword() {
	return SecurityUtil.decryptDES(super.getPassword());
    }
}
