package com.microservicio_laboratorios.microservicio_laboratorios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MicroservicioLaboratoriosApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void main() {
		MicroservicioLaboratoriosApplication.main(new String[] { "--server.port=0" });
	}

}
