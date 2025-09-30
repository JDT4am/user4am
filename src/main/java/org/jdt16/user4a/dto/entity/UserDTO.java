package org.jdt16.user4a.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jdt16.user4a.utility.ColumnNameEntityUtility;
import org.jdt16.user4a.utility.TableNameEntityUtility;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = TableNameEntityUtility.TABLE_USERS)
public class UserDTO {

    @Id
    @Column(name = ColumnNameEntityUtility.COLUMN_USERS_ID, nullable = false, updatable = false)
    private UUID userEntityDTOId;

    @Column(name = ColumnNameEntityUtility.COLUMN_USERS_NAME, nullable = false)
    private String userEntityDTOName;

    @Column(name = ColumnNameEntityUtility.COLUMN_USERS_AGE, nullable = false)
    private String userEntityDTOAge;

    @Column(name = ColumnNameEntityUtility.COLUMN_USERS_EMAIL, nullable = false)
    private String userEntityDTOEmail;

    @Column(name = ColumnNameEntityUtility.COLUMN_USERS_GENDER, nullable = false)
    private Integer userEntityDTOGender;

    @Column(name = ColumnNameEntityUtility.COLUMN_USERS_CREATED_STATUS, nullable = false)
    private Integer userEntityDTOStatus;

}
