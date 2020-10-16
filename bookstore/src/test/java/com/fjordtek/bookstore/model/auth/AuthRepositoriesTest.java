// Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.SecureRandom;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * TODO: N/A
 *
 * @author Pekka Helenius
 */

@RunWith(SpringRunner.class)
@DataJpaTest
@TestMethodOrder(Alphanumeric.class)
public class AuthRepositoriesTest {

	@Autowired
	private RoleRepository     roleRepository;

	@Autowired
	private UserRepository     userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	private PasswordEncoder    passwordEncoder = new BCryptPasswordEncoder(14, new SecureRandom());

	//////////////////////////////

	@Test
	public void testA_CreateRole() {
		Role roleA = roleRepository.save(new Role("TEST"));
		assertThat(roleA).isNotNull();
	}

	@Test
	public void testB_CreateRoleWithoutNameShouldFail() {
		assertThrows(
				ConstraintViolationException.class, () -> {
					roleRepository.save(new Role());
					roleRepository.findAll().forEach(System.out::print);
				}
				);
	}

	@Test
	public void testC_CreateRoleDuplicateNameShouldFail() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {

					roleRepository.save(new Role("FOO"));
					roleRepository.save(new Role("FOO"));

					roleRepository.findAll().forEach(System.out::print);
				});
	}

	@Test
	public void testD_DeleteUnusedRole() {

		Role roleA = roleRepository.save(new Role("FOO"));
		Long id = roleA.getId();
		roleRepository.delete(roleA);

		assertThat(roleRepository.findById(id)).isEmpty();

	}

	@Test
	public void testE_ChangeRoleName() {

		String nameA = "FOO";
		String nameB = "BAR";

		Role roleA = roleRepository.save(new Role(nameA));
		roleA.setName(nameB);
		roleRepository.save(roleA);

		assertThat(roleA.getName().equals(nameA)).isEqualTo(false);

		//System.out.println(roleRepository.findById(roleA.getId()).get().getName());

	}

	//////////////////////////////

	@Test
	public void testF_CreateUser() {
		User userA = userRepository.save(new User(
				"foo",
				passwordEncoder.encode("bar"),
				"foo.bar@fjordtek.com"
				)
				);

		assertThat(userA).isNotNull();
	}

	@Test
	public void testG__CreateUserWithEmptyPasswordShouldFail() {

		/*
		 * NOTE: null password must be forced for empty/blank passwords
		 * if sign up form is implemented.
		 * BCryptPasswordEncoder encode method checks for null value only
		 * and throws exception accordingly. It does not work on empty
		 * ("") values.
		 *
		 */

		assertThrows(
				IllegalArgumentException.class, () -> {
					userRepository.save(new User(
							"bar",
							passwordEncoder.encode(null),
							"bar@fjordtek.com"
							)
							);
				});

	}

	@Test
	public void testH__CreateUserWithInvalidEmailShouldFail() {

		assertThrows(
				ConstraintViolationException.class, () -> {
					userRepository.save(new User(
							"foo",
							passwordEncoder.encode("bar is foo"),
							"bar//foobar.com"
							)
							);
					userRepository.findAll().forEach(System.out::print);
				});

		assertThrows(
				ConstraintViolationException.class, () -> {
					userRepository.save(new User(
							"foo",
							passwordEncoder.encode("bar is foo"),
							"bar||foobar.com"
							)
							);
					userRepository.findAll().forEach(System.out::print);
				});
	}

	@Test
	public void testI_CreateUserWithEmptyUsernameShouldFail() {

		assertThrows(
				ConstraintViolationException.class, () -> {
					userRepository.save(new User(
							"",
							passwordEncoder.encode("bar is foo"),
							"bar@foobar.com"
							)
							);
					userRepository.findAll().forEach(System.out::print);
				});

	}

	@Test
	public void testJ_CreateUserDuplicateUsernamesShouldFail() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {

					userRepository.save(new User(
							"foo",
							passwordEncoder.encode("foo"),
							"foo@foo.com"
							)
							);

					userRepository.save(new User(
							"foo",
							passwordEncoder.encode("bar"),
							"bar@bar.com"
							)
							);

					userRepository.findAll().forEach(System.out::print);
				});

	}

	@Test
	public void testK_DeleteExistingUser() {

		User userA = userRepository.save(new User(
				"foo",
				passwordEncoder.encode("foo"),
				"foo@foo.com"
				)
				);
		Long id = userA.getId();
		userRepository.delete(userA);

		assertThat(userRepository.findById(id)).isEmpty();

	}

	@Test
	public void testL_ChangeUserName() {

		String nameA = "foo";
		String nameB = "bar";

		User userA = userRepository.save(new User(
				nameA,
				passwordEncoder.encode("foo"),
				"foo@foo.com"
				)
				);
		userA.setUsername(nameB);
		userRepository.save(userA);

		assertThat(userA.getUsername().equals(nameA)).isEqualTo(false);

		//System.out.println(userRepository.findById(userA.getId()).get().getUsername());

	}

	//////////////////////////////

	@Test
	public void testK_JoinAddExistingRoleAndUser() {

		User userA = userRepository.save(new User(
				"foo",
				passwordEncoder.encode("foo"),
				"foo@foo.com"
				)
				);
		Role roleA = new Role("FOO");

		UserRole userRoleA = new UserRole(userA, roleA);
		userRoleRepository.save(userRoleA);

		assertThat(userRoleA).isNotNull();

	}

	@Test
	public void testL_JoinSetRoleNullKeepUserShouldFail() {

		assertThrows(
				JpaSystemException.class, () -> {

					User userA = userRepository.save(new User(
							"foo",
							passwordEncoder.encode("foo"),
							"foo@foo.com"
							)
							);

					userRepository.save(userA);
					userRoleRepository.save(new UserRole(userA, null));
				});

	}

	@Test
	public void testM_JoinSetUserNullKeepRoleShouldFail() {

		assertThrows(
				JpaSystemException.class, () -> {
					Role roleA = new Role("FOO");

					roleRepository.save(roleA);
					userRoleRepository.save(new UserRole(null, roleA));
				});

	}

	@Test
	public void testN_JoinAddDuplicatePrimaryKeyShouldFail() {

		assertThrows(
				DataIntegrityViolationException.class, () -> {

					User userA = userRepository.save(new User(
							"foo",
							passwordEncoder.encode("foo"),
							"foo@foo.com"
							)
							);
					Role roleA = new Role("FOO");

					userRepository.save(userA);
					roleRepository.save(roleA);

					userRoleRepository.save(new UserRole(userA, roleA));
					userRoleRepository.save(new UserRole(userA, roleA));
				});

	}

	@Test
	public void testO_JoinDeleteExistingRoleAndUser() {

		User userA = userRepository.save(new User(
				"foo",
				passwordEncoder.encode("foo"),
				"foo@foo.com"
				)
				);
		Role roleA = new Role("FOO");

		userRepository.save(userA);
		roleRepository.save(roleA);
		UserRole userRoleA = new UserRole(userA, roleA);
		userRoleRepository.save(userRoleA);

		userRoleRepository.deleteByCompositeId(userA.getId(), roleA.getId());

		assertThat(userRoleRepository.findByCompositeId(userA.getId(), roleA.getId())).isNull();

	}

}
