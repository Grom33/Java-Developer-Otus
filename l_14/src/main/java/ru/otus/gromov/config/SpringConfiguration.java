package ru.otus.gromov.config;

/*
 * Created by Gromov Vitaly (Grom33), 2018
 * e-mail: mr.gromov.vitaly@gmail.com
 */

import com.fasterxml.jackson.databind.ObjectMapper;

class SpringConfiguration {


	public static ObjectMapper getObjectMapperBean(){
		return new ObjectMapper();
	}




}
