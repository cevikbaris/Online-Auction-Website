import React from 'react';
import { Link } from 'react-router-dom'
import ProfileImageWithDefault from './ProfileImageWithDefault';

const UserInList = (props) => {
    const { user } = props;
    const { username, name, image } = user;

    return (
        
        <Link to={`/user/${username}`} 
        className='list-group-item list-group-item-action'>

            <ProfileImageWithDefault 
                className='rounded-circle'
                width='32' height='32' 
                image={image} />
            
            <span className='pl-2'>{name} - {username}</span>

            {/* <Link className="material-icons btn ml-5 btn-primary" to={`/admin/edit/${name}`} >edit</Link> */}

        </Link>
        
          
    );
};

export default UserInList;