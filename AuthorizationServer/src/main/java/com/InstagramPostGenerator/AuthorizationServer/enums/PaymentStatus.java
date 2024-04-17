package com.InstagramPostGenerator.AuthorizationServer.enums;

/*
    PENDING - payment initialized but not processed yet
    COMPLETED - payment successfully completed
    FAILED - payment failed
    CANCELED - payment canceled by system or user
 */
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELED
}
