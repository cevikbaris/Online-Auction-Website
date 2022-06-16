import React, { useEffect, useState } from 'react';
import ProfileCard from '../Components/ProfileCard';
import { getUser } from '../Api/ApiCalls';
import {useParams} from 'react-router-dom';
import { useApiProgress } from '../Shared/ApiProgress';
import Spinner from '../Components/Spinner';

const UserPage = props => {

  const [user, setUser] = useState({});
  const [notFound, setNotFound] = useState(false);
  const { username } = useParams();  // props.match.params;
  const pendingApiCall = useApiProgress('get', '/users/' + username, true);

  useEffect(() => {
    setNotFound(false);
  }, [user])

  useEffect(() => {
    const loadUser = async () => {
      try {
        const response = await getUser(username);
        setUser(response.data);
      }
      catch (error) {
        setNotFound(true);
      }
    }
    loadUser();
  }, [username]); //url parameter 


  if (notFound) {
    return (
      <div className="container">
        <div className="alert alert-danger text-center">
          <div>
            <i className="material-icons" style={{ fontSize: '48px' }}>
              error
            </i>
          </div>
          User not found
        </div>
      </div>
    );
  }

  if (pendingApiCall || user.username !== username) {
    return <Spinner />
  }


  return (
    <div className="container">
      <ProfileCard user={user} />
    </div>
  );
};

export default UserPage;