import React, { useEffect, useState } from 'react';
import { getAllIdentity } from '../Api/ApiCalls';
import { Link } from 'react-router-dom';
import './verifyList.css';
import Modal from './Modal';
import ButtonWithProgress from './ButtonWithProgress';

const VerifyList = () => {

    const [identities, setIdentities] = useState([]);
    const [error, setError] = useState();


    useEffect(() => {
        loadIdentities();
    }, []);

    const loadIdentities = async () => {
        try {
            const response = await getAllIdentity();
            setIdentities(response.data);
        }
        catch (error) {
            setError(error.response.data);
        }
    }


    return (
        <div className='card' id='scroll'>
            <h3 className='card-header'>Authentication Requests</h3>
            <ul className='list-group-flush'>
                {identities && identities.map(identity => (
                    <Link to={`/identity/${identity.idNumber}` } className='list-group-item list-group-item-action'key={identity.id} >
                        <span>Authentication Request :   ID={identity.idNumber}</span>
                    </Link>
                ))}
                {!identities && <h5 className='text-center text-danger'> No Authentication Requests</h5>}
            </ul>
            {error && <div className='text-center text-danger'> Failed to load requests </div>}
        </div>
    );
};

export default VerifyList;