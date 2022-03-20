package edu.idol.mca.bcs.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name= "Users")
public class User {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message = "Please Enter Name")
	private String name;
	
	@NotBlank(message = "Please Enter Login Name")
	@Column(unique = true, updatable = false)
	private String loginName;
	
	@NotBlank(message = "Please Enter Password")
	@Size(min=8, max=20, message = "Please enter password of size minimum 8 and maximum 20")
	private String pwd;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "user")
	private List<Address> address=new ArrayList<>();
	
	public User() {
		super();
	}
	
	
	public User(long id, String name, String loginName, String pwd) {
		super();
		this.id = id;
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
	}


	public User(long id, String name, String loginName, String pwd, List<Address> address) {
		super();
		this.id = id;
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
		this.address = address;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getPwd() {
		return pwd;
	}


	public void setPwd(String pwd) {
		this.pwd = pwd;
	}


	public List<Address> getAddress() {
		return address;
	}


	public void setAddress(List<Address> address) {
		this.address = address;
	}
	
}
