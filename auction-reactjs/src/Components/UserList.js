import React, { useState } from 'react'
import { useEffect } from 'react';
import { getUsers } from '../Api/ApiCalls';
import Spinner from './Spinner'
import { useApiProgress } from '../Shared/ApiProgress';
import UserInList from './UserInList'

const UserList = () => {

    const [page, setPage] = useState({
        content: [],
        size: 5,
        number: 0
    });

    const [loadFailure, setLoadFailure] = useState(false);
    
    const pendingApiCall = useApiProgress('get','/users?page');

    useEffect(() => {
        loadUsers();
    }, []); //componentDidMount() call if array empty.

    const loadUsers =async page => {
        setLoadFailure(false);
        try{
            const response = await getUsers(page);
            setPage(response.data);  
        } catch(error){
            setLoadFailure(true);
        }
        
    }

    const onClickNext = () => {
        const nextPage = page.number + 1;
        loadUsers(nextPage);
    }
    const onClickPrevious = () => {
        const previousPage = page.number - 1;
        loadUsers(previousPage);
    }


    //last first component içinde varlar componente bak page içinde
    const { content: users, last, first } = page;

    let actionDiv = (
        <div>
            {first === false && (
                <button className="btn btn-sm btn-light" onClick={onClickPrevious}>
                    Previous
                </button>
            )}
            {last === false && (
                <button className="btn btn-sm btn-light float-right" onClick={onClickNext}>
                    Next
                </button>
            )}
        </div>
    );

    if (pendingApiCall) {
        actionDiv = <Spinner/>
    }


    return (
        <div className='card'>
            <h3 className='card-header'>Users</h3>
            <div className='list-group-flush'>
                {users.map(user => (
                    <UserInList key={user.username} user={user} />
                    
                ))}
            </div>
            {actionDiv}
            {loadFailure && <div className='text-center text-danger'> Failed to load users </div>}
        </div>
    )

}
export default UserList;