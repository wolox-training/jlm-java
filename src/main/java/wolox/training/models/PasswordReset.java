package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PasswordReset {

    @NotNull
    @ApiModelProperty(required = true, notes = "Old password of the user")
    private String oldPassword;

    @NotNull
    @ApiModelProperty(required = true, notes = "New password of the user")
    private String newPassword;

    public void setOldPassword(String oldPassword) {

        checkNotNull(oldPassword, "Please check oldPassword field, its null");

        this.oldPassword = oldPassword;

    }

    public void setNewPassword(String newPassword) {

        checkNotNull(newPassword, "Please check newPassword field, its null");
        checkArgument(!oldPassword.equals(newPassword), "Passwords must not be the same");

        this.newPassword = newPassword;

    }

}
