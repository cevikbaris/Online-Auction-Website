import React from 'react';


const ProfileImageWithDefault = (props) => {
    
    const defaultPicture= 'https://www.pngall.com/wp-content/uploads/5/Profile-PNG-File.png';

    const { image , tempimage} =props;

    let imageSource = defaultPicture;

    if(image){ 
        imageSource =  'images/profile/' +image;//'images/profile/'+image;
    }               //for backend -> registry.addResourceHandler("/images/**") 

  

    return (
        <img
        alt={`Profile`}
        src={tempimage || imageSource}
        {...props}
        onError={event => {
          event.target.src = defaultPicture; //if the image is not found, then set the default image
        }}
      />
    );
};

export default ProfileImageWithDefault;