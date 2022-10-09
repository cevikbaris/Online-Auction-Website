package com.app.entity;

import com.app.dto.UserRequest;
import com.app.shared.UniqueUsername;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@ToString
@RequiredArgsConstructor
@Setter
@Getter
public class User  implements UserDetails{

	private static final long serialVersionUID = -2858049706581317087L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;

    @Column(nullable = false)
	private String name;

	@UniqueUsername
    @Column(nullable = false)
	private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
	@JsonIgnore
	private String password;


	@OneToMany(mappedBy = "bidder",cascade = CascadeType.ALL)
	@JsonIgnore
	@ToString.Exclude
	private List<Bid> bids;
	
	@OneToMany(mappedBy = "creator",cascade = CascadeType.ALL)
	@JsonManagedReference(value="sell")
	@ToString.Exclude
	private List<Auction> productsOnSale;
	

	@OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL)
	@ToString.Exclude
	private List<Auction> biddedProducts;
	
	
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	private String image;
	
	private boolean isApproved;
	

	//------------------------------

	
	public User(UserRequest user) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User user = (User) o;
		return false;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
