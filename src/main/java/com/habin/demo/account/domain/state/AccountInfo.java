package com.habin.demo.account.domain.state;

import com.habin.demo.account.adapter.output.persistence.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.habin.demo.account.adapter.output.persistence.AccountJpaEntity}
 */
public record AccountInfo(
        LocalDateTime createdAt,
        @NotNull @Size(max = 255) String uuid,
        @Size(max = 255) String username,
        List<Role> roles) {
}