package com.longstudios.weekbites;

import com.longstudios.weekbites.entidades.*;
import com.longstudios.weekbites.repositorios.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootApplication
public class WeekbitesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeekbitesApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(TipoDietaRepository tipoDietaRepository, RolRepository rolRepository, UsuarioRepository usuarioRepository, PermisoRepository permisoRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			/*TipoDieta estandar = new TipoDieta();
			estandar.setNombreTD("Estándar");
			estandar.setDescripcionTD("Comidas balanceadas para una alimentación variada.");
			tipoDietaRepository.save(estandar);

			TipoDieta vegetariano = new TipoDieta();
			vegetariano.setNombreTD("Vegetariano");
			vegetariano.setDescripcionTD("Preparaciones sin origen animal, ricas en nutrientes.");
			tipoDietaRepository.save(vegetariano);

			TipoDieta celiaco = new TipoDieta();
			celiaco.setNombreTD("Celíaco");
			celiaco.setDescripcionTD("Platos sin gluten, aptos para personas con celiaquía.");
			tipoDietaRepository.save(celiaco);

			TipoDieta diabetico = new TipoDieta();
			diabetico.setNombreTD("Diabético");
			diabetico.setDescripcionTD("Opciones sin azúcares añadidos y controladas en carbohidratos.");
			tipoDietaRepository.save(diabetico);

			Rol admin = new Rol();
			admin.setNombre("ADMIN");
			rolRepository.save(admin);

			Usuario usuario = new Usuario();
			usuario.setEmail("admin@weekbites.com");
			usuario.setPassword(passwordEncoder.encode("admin123"));
			usuario.setRol(admin);
			usuarioRepository.save(usuario);

			List<String> nombresPermisosCliente = List.of(
					"MODIFICAR_SUSCRIPCION", "SUSPENDER_SUSCRIPCION",
					"REACTIVAR_SUSCRIPCION","HISTORIAL_SUSCRIPCION",
					"RECUPERAR_CONTRASEÑA", "MODIFICAR_DATOS",
					"VALORAR_VIANDAS", "DETALLE_VIANDA", "VISUALIZAR_MENU"
			);

			Set<Permiso> permisosCliente = new HashSet<>();
			for (String nombre : nombresPermisosCliente) {
				Permiso permiso = permisoRepository.findByNombre(nombre)
						.orElseGet(() -> permisoRepository.save(new Permiso(null, nombre)));
				permisosCliente.add(permiso);
			}

			Rol rolCliente = new Rol();
			rolCliente.setNombre("CLIENTE")
			rolCliente.setPermisos(permisosCliente);

			rolRepository.save(rolCliente);

			List<String> nombresPermisosAdmin = List.of(
					"RECUPERAR_CONTRASEÑA", "MODIFICAR_DATOS", "GESTIONAR_VIANDA", "GESTIONAR_DIETA",
					"GESTIONAR_MENU", "GESTIONAR_ROLES", "GESTIONAR_USUARIOS", "VISUALIZAR_REPORTES"
			);

			Set<Permiso> permisosAdmin = new HashSet<>();
			for (String nombre : nombresPermisosAdmin) {
				Permiso permiso = permisoRepository.findByNombre(nombre)
						.orElseGet(() -> permisoRepository.save(new Permiso(null, nombre)));
				permisosAdmin.add(permiso);
			}

			Rol rolAdmin = rolRepository.findByNombre("ADMIN")
					.orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

			rolAdmin.setPermisos(permisosAdmin);

			rolRepository.save(rolAdmin);

			List<String> nombresPermisosCocinero = List.of(
					"RECUPERAR_CONTRASEÑA", "MODIFICAR_DATOS", "DETALLE_VIANDA",
					"VISUALIZAR_MENU", "CONSULTAR_VIANDAS_A_PREPARAR", "CONFIRMAR_PREPARACION",
					"CONSULTAR_VALORACIONES"
			);

			Set<Permiso> permisosCocinero = new HashSet<>();
			for (String nombre : nombresPermisosCocinero) {
				Permiso permiso = permisoRepository.findByNombre(nombre)
						.orElseGet(() -> permisoRepository.save(new Permiso(null, nombre)));
				permisosCocinero.add(permiso);
			}

			Rol rolCocinero = new Rol();
			rolCocinero.setNombre("COCINERO");
			rolCocinero.setPermisos(permisosCocinero);

			rolRepository.save(rolCocinero);

			List<String> nombresPermisosRepartidor = List.of(
					"RECUPERAR_CONTRASEÑA", "MODIFICAR_DATOS", "CONSULTAR_VIANDAS_A_ENTREGAR",
					"CONFIRMAR_ENTREGA", "CONSULTAR_VALORACIONES"
			);

			Set<Permiso> permisosRepartidor = new HashSet<>();
			for (String nombre : nombresPermisosRepartidor) {
				Permiso permiso = permisoRepository.findByNombre(nombre)
						.orElseGet(() -> permisoRepository.save(new Permiso(null, nombre)));
				permisosRepartidor.add(permiso);
			}

			Rol rolRepartidor = new Rol();
			rolRepartidor.setNombre("REPARTIDOR");
			rolRepartidor.setPermisos(permisosRepartidor);

			rolRepository.save(rolRepartidor);*/

		};
	}


}
