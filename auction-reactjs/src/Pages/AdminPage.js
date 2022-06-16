import React from 'react';
import UserList from '../Components/UserList';
import VerifyList from '../Components/VerifyList';

const AdminPage = () => {
    return (
        <div className='container'>
            <UserList/>
            <br></br>
            <VerifyList/>
        </div>
    );
};

export default AdminPage;