import React, { useEffect, useState } from 'react';
import { getByIdIdentity } from '../Api/ApiCalls';
import { useParams } from 'react-router-dom';
import ButtonWithProgress from './ButtonWithProgress';
import { approveUser, deleteIdentity } from '../Api/ApiCalls';
import Modal from './Modal';
import { useApiProgress } from '../Shared/ApiProgress';


const UserAuth = (props) => {

    const { idNumber } = useParams();
    const [identity, setIdentity] = useState();
    const [modalVisible, setModalVisible] = useState(false);
    const [modalReject, setModalReject] = useState(false);

    useEffect(() => {
        loadPage();
    }, []);

    const loadPage = async () => {
        try {
            const response = await getByIdIdentity(idNumber);
            setIdentity(response.data);
        }
        catch (error) {
            console.log(error);
            setIdentity(undefined);
        }
    }

    const onClickConfirm = async () => {
        try {
            const response = await approveUser(idNumber);
            console.log(response);
            props.history.push('/admin')

        } catch (error) {
            console.log(error);
        }

    }

    const onClickReject = async () => {
        try {
            await deleteIdentity(idNumber);
            props.history.push('/admin')

        } catch (error) {
            console.log(error);
        }
    }


    const pendingApiCall = useApiProgress('put', `/users/update/${idNumber}`);
    const pendingApiCallReject = useApiProgress('delete', `/identity/user/${idNumber}`);

    return (
        <div className='container card' style={{ background: '#f3f5f5' }}>
            <h2 className='text-center card-header'>User ID Authentication</h2>
            <div className='card-body'>
                {identity && <img className='img-thumbnail' width="600" height="600"
                    src={'/images/identity/' + identity.imageId} alt={identity.id} />}
                <br /><br />
                <h4>ID Number =  {identity && identity.idNumber} </h4>
                <hr />

                <ButtonWithProgress onClick={() => setModalVisible(true)} text='Confirm User' className='my-4 btn btn-success' />
                <ButtonWithProgress onClick={() => setModalReject(true)} text='Reject' className='my-4 btn btn-danger ml-5' />
            </div>
            <Modal visible={modalVisible}
                onClickCancel={() => setModalVisible(false)}
                onClickOk={onClickConfirm}
                pendingApiCall={pendingApiCall}
                title='Authentication Confirmation'
                okButtonText='Approve ID'
                message={
                    <div>
                        <div>
                            <strong>User will be approved. </strong>
                        </div>
                    </div>
                } />
            <Modal visible={modalReject}
                onClickCancel={() => setModalReject(false)}
                onClickOk={onClickReject}
                pendingApiCall={pendingApiCallReject}
                title='Authentication Confirmation'
                okButtonText='Reject ID'
                message={
                    <div>
                        <div>
                            <strong>User will be rejected. </strong>
                        </div>
                    </div>
                } />
        </div>
    );
};

export default UserAuth;