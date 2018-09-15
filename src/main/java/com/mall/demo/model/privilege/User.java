package com.mall.demo.model.privilege;

import com.mall.demo.common.utils.StringUtils;
import com.mall.demo.common.utils.UserUtils;
import com.mall.demo.model.base.BaseTO;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends BaseTO implements Serializable {

    public static final byte USER_STATE_COMMON = 1;
    public static final byte USER_STATE_LOCKED = 2;
    public static final byte USER_STATE_DEACTIVE = 3;

    @Column(unique =true)
    private String account;//帐号
    private String nickname;//名称（昵称或者真实姓名，不同系统不同定义）
    private String password; //密码;
    private String salt;//加密密码的盐
    private String avatar;
    @Transient
    private String avatarPath;

    private String phoneNumber;

    private String email;
    /**
     * 是否是管理员
     */
    private Boolean admin = false;
    /**
     * 逻辑删除flag
     */
    private Boolean deleted = Boolean.FALSE;
    private byte state = USER_STATE_COMMON;//1:正常状态,2：用户被锁定. 3:用户未激活

    @ManyToMany(fetch= FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    private List<SysRole> roleList;// 一个用户具有多个角色

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    @ApiModelProperty(value="关注",name="attentionUsers")
    @ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name = "UserAttention", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns ={@JoinColumn(name = "attentionUserId") })
    private List<User> attentionUsers;

    @ApiModelProperty(value="被关注",name="beAttentionUsers")
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name = "UserAttention", joinColumns = { @JoinColumn(name = "attentionUserId") }, inverseJoinColumns ={@JoinColumn(name = "userId") })
    private List<User> beAttentionUsers;

    @Transient
    private boolean AttentionFlag;

    public String getCredentialsSalt(){
        if(!StringUtils.isEmpty(email)){
            return this.email+this.salt;
        }else if(!StringUtils.isEmpty(phoneNumber)){
            return this.phoneNumber + this.salt;
        }
        return this.account+this.salt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public List<User> getAttentionUsers() {
        return attentionUsers;
    }

    public void setAttentionUsers(List<User> attentionUsers) {
        this.attentionUsers = attentionUsers;
    }

    public List<User> getBeAttentionUsers() {
        return beAttentionUsers;
    }

    public void setBeAttentionUsers(List<User> beAttentionUsers) {
        this.beAttentionUsers = beAttentionUsers;
    }

    public void setAttentionFlag(boolean attentionFlag) {
        AttentionFlag = attentionFlag;
    }

    public boolean isAttentionFlag() {
        User user = UserUtils.getCurrentUser();
        if(user != null && beAttentionUsers != null){
            for (User beAttentionUser : beAttentionUsers) {
                if(beAttentionUser.getId().longValue() == user.getId().longValue()){
                    return true;
                }
            }

        }
        return AttentionFlag;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
