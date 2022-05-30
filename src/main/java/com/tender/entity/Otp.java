package com.tender.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="otp_data")
public class Otp {

    @Id
    @Column(name="otp_id")
    private String otpId;

    @Column(name="otp")
    private String otp;

    @Column(name="user_id")
    private String userId;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="verification_attempts")
    private Integer verificationAttempts;

    @Column(name="resend_attempts")
    private Integer resendAttempts;

    @Column(name="is_valid")
    private Boolean isValid;

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVerificationAttempts() {
        return verificationAttempts;
    }

    public void setVerificationAttempts(Integer verificationAttempts) {
        this.verificationAttempts = verificationAttempts;
    }

    public Integer getResendAttempts() {
        return resendAttempts;
    }

    public void setResendAttempts(Integer resendAttempts) {
        this.resendAttempts = resendAttempts;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
