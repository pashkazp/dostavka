package ua.com.sipsoft.dao.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.com.sipsoft.util.security.Role;

/**
 * Simple JavaBeen domain object that represents User.
 *
 * @author Pavlo Degtyaryev
 * @version 1.0
 */
@EqualsAndHashCode(of = { "id" })
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8186778697067301448L;

	/** The id. */
	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	// @GeneratedValue(strategy = GenerationType.SEQUENCE)
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
	private Long id;

	/** The version. */
	@Version
	@Column(columnDefinition = "integer DEFAULT 0", nullable = false)
	private Long version = 0L;

	/** The email. */
	@Column(name = "email", unique = true, length = 100, nullable = false)
	private String email = "";

	/** The display name of User. */
	@Column(name = "name", unique = false, length = 75, nullable = false)
	private String name = "";

	/** The enabled. */
	@Column(name = "enabled", nullable = false)
	private Boolean enabled = true;

	/** The password. */
	@JsonIgnore
	@Column(name = "password", length = 255, nullable = false)
	private String password = "";

	/** The verified. */
	@Column(name = "verified", nullable = false)
	private Boolean verified = false;

	/** The roles. */
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "user_role", nullable = false, length = 25)
	@Enumerated(EnumType.STRING)
	@Fetch(FetchMode.JOIN)
	private Set<Role> roles = new HashSet<>();

	/**
	 * Sets the roles.
	 *
	 * @param roles the new roles
	 */
	public void setRoles(Iterable<Role> roles) {
		this.roles.clear();
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	public void addRoles(Iterable<Role> roles) {
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	public void addRoles(Role... roles) {
		for (Role role : roles) {
			this.roles.add(role);
		}
	}

	/**
	 * Contains any role.
	 *
	 * @param roles the roles
	 * @return true, if successful
	 */
	public boolean hasRoles(Collection<Role> roles) {
		if (roles != null && this.roles != null) {
			for (Role role : roles) {
				if (this.roles.contains(role))
					return true;
			}
		}
		return false;
	}

	public boolean hasRoles(Role... roles) {
		if (roles != null && this.roles != null) {
			for (Role role : roles) {
				if (this.roles.contains(role))
					return true;
			}
		}
		return false;
	}

	public boolean hasNoOneRole(Collection<Role> roles) {
		return !hasRoles(roles);
	}

	public boolean hasNoOneRole(Role... roles) {
		return !hasRoles(roles);
	}

	/**
	 * Instantiates a new user.
	 *
	 * @param that the that
	 */
	public User(User that) {
		this.id = that.id;
		this.version = that.version;
		this.name = that.name;
		this.email = that.email;
		this.enabled = that.enabled;
		this.verified = that.verified;
		this.password = that.password;
		this.roles = new HashSet<>(that.roles);
	}

	/**
	 * Gets the highest role. Where Admin - highest and User - lowest. If Role is
	 * absent return User
	 *
	 * @return the highest {@link Role}
	 */
	public Role getHighesRole() {
		for (Role role : Role.values()) {
			if (roles.contains(role)) {
				return role;
			}
		}
		return Role.ROLE_USER;
	}

	private int getPasswordLength() {
		return password == null ? 0 : password.length();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=").append(id).append(", version=").append(version).append(", email=").append(email)
				.append(", name=").append(name).append(", enabled=").append(enabled).append(", password=")
				.append("*".repeat(getPasswordLength())).append(", verified=").append(verified).append(", roles=")
				.append(roles).append("]");
		return builder.toString();
	}

}