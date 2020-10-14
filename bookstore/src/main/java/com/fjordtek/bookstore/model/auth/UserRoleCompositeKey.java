//Pekka Helenius <fincer89@hotmail.com>, Fjordtek 2020

package com.fjordtek.bookstore.model.auth;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * This class implements a composite primary key based on
 * primary keys of User & Role entities.
 * <p>
 * This class overrides several JPA specific methods.
 *
 * @author Pekka Helenius
 */

@Embeddable
public class UserRoleCompositeKey implements Serializable {

	private static final long serialVersionUID = 2889337731246989510L;

	@NotNull
	private Long userId;
	@NotNull
	private Long roleId;

	////////////////////
	// Attribute setters

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	////////////////////
	// Attribute getters

	public Long getUserId() {
		return userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	////////////////////
	// Class constructors

	public UserRoleCompositeKey() {
	}

	public UserRoleCompositeKey(Long userId, Long roleId) {
		// super();
		this.userId = userId;
		this.roleId = roleId;
	}

	////////////////////
	// Class overrides

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
        	return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false;
        }

        UserRoleCompositeKey that = (UserRoleCompositeKey) obj;

        return Objects.equals(userId, that.userId) &&
               Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

	@Override
	public String toString() {
		return "[" + "user_id: " + this.userId + ", " +
				"role_id: "      + this.roleId + "]";
	}

}
