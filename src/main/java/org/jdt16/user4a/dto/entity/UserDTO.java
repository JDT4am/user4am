package org.jdt16.user4a.dto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
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
    @GeneratedValue(generator = "UUID")
    @Column(name = ColumnNameEntityUtility.COLUMN_USER_ID, nullable = false, updatable = false)
    private UUID userEntityDTOId;

    @Column(name = ColumnNameEntityUtility.COLUMN_USER_NAME, nullable = false)
    private String userEntityDTOName;

    @Column(name = ColumnNameEntityUtility.COLUMN_USER_AGE, nullable = false)
    private Integer userEntityDTOAge;

    @Column(name = ColumnNameEntityUtility.COLUMN_USER_EMAIL, nullable = false, unique = true)
    private String userEntityDTOEmail;

    @Column(name = ColumnNameEntityUtility.COLUMN_USER_GENDER, nullable = false)
    private Integer userEntityDTOGender;

    @Column(name = ColumnNameEntityUtility.COLUMN_USER_STATUS, nullable = false)
    private Integer userEntityDTOStatus;
}
