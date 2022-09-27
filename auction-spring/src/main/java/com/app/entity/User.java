package com.app.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.dto.UserWithoutRoleDto;
import com.app.shared.UniqueUsername;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class User  implements UserDetails{

	private static final long serialVersionUID = -2858049706581317087L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	
	@NotNull(message="Name can not be null")
	@Size(min = 2, max=50, message = "Name must be more than 2 letters ")
    @Column(nullable = false)
	private String name;
	

	@NotNull(message="Username can not be null")
	@UniqueUsername
	@Size(min=5,max=100  )
    @Column(nullable = false)
	private String username;
	
    @NotNull(message="Email can not be null")
    @Column(nullable = false)
    private String email;
	
	
	@NotNull(message="Password can not be null")
	@Size(min = 4, max=16 , message = "Password must be a minimum of 4 characters and a maximum of 16 characters. ")
    @Column(nullable = false)
	@JsonIgnore
	private String password;

	
	
	@OneToMany(mappedBy = "bidder",cascade = CascadeType.ALL)
	//@JsonManagedReference(value="bidder")
	@JsonIgnore
	private List<Bid> bids;
	
	@OneToMany(mappedBy = "creator",cascade = CascadeType.ALL)
	@JsonManagedReference(value="sell")

	private List<Auction> productsOnSale;
	

	@OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL)
	//@JsonManagedReference(value="buyer")
	//@JsonIgnore
	private List<Auction> biddedProducts;
	
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
	joinColumns = @JoinColumn(name = "user_id"), 
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	private String image;
	
	private boolean isApproved;
	
	
	
	
	
	//------------------------------

	
	public User() {
		
	}
	
	public User(UserWithoutRoleDto user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.username=user.getUsername();
		this.image=user.getImage();
	}
	

   @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	    Collection<Role> roles = getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
         
        return authorities;
    }

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}


	public void setProductsOnSale(List<Auction> productsOnSale) {
		this.productsOnSale = productsOnSale;
	}


	public void setBiddedProducts(List<Auction> biddedProducts) {
		this.biddedProducts = biddedProducts;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	
}
