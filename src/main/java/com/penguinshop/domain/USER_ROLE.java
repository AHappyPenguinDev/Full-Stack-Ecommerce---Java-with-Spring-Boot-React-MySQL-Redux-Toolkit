package com.penguinshop.domain;

/**
 * <p>Defines the possible roles a {@link com.penguinshop.model.User} can be.<br>
 * Roles include: <br></p>
 * <ul>
 *  <li>ROLE_ADMIN
 *  <li>ROLE_CUSTOMER
 *  <li>ROLE_SELLER
 * </ul>
 */
public enum USER_ROLE{
    //Using enum so that if user tries to use any other role,
    //enum throws an error
    ROLE_ADMIN,
    ROLE_CUSTOMER,
    ROLE_SELLER
}
