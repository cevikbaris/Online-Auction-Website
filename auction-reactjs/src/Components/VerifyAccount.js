import React from 'react';
import Input from './Input';
import { useEffect, useState } from 'react';
import ButtonWithProgress from './ButtonWithProgress';
import { postIdImage, putIdentity } from '../Api/ApiCalls';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';


const VerifyAccount = (props) => {

    const [error, setError] = useState();
    const [newImage, setNewImage] = useState();
    const [idNumber, setIdNumber] = useState();
    const [imageId, setImageId] = useState();
    const [imageError, setImageError] = useState();
    const [idError, setIdError] = useState();
    const { username: loggedInUsername } = useSelector(store => ({ username: store.username }));
    const numberRegex = /^[0-9]+$/;

    const routeParams = useParams();
    const pathUsername = routeParams.username;

    const isOwner = loggedInUsername === pathUsername;

    useEffect(() => {
        setError(undefined)
    }, [newImage, idNumber]);

    useEffect(() => {
        setImageError(undefined)
    }, [imageId]);

    const onChangeFile = (event) => {
        if (event.target.files.length < 1) {
            return;
        }
        const file = event.target.files[0];
        const fileReader = new FileReader();
        fileReader.onload = () => {
            setNewImage(fileReader.result);
            uploadFile(file);
        }
        fileReader.readAsDataURL(file);
    }

    const uploadFile = async file => {
        const attachment = new FormData();
        attachment.append('file', file);
        try {
            const response = await postIdImage(attachment);
            setImageId(response.data.id);
            setImageError(undefined);
        } catch (error) {
            setImageError(error.response.data);
        }
    };


    const saveId = async () => {
        if (!idNumber.match(numberRegex)) {
            setError("Id number must be numeric");
            return;
        }

        const body = {
            idNumber,
            imageId,
            loggedInUsername
        }
        try {
            await putIdentity(body)
            setError(undefined)
            props.history.push('/');
        } catch (error) {
            setError(error.response.data);
        }

    }

    const onChangeIdNumber = (event) => {
        let digit = Math.max(Math.floor(Math.log10(Math.abs(event.target.value))), 0) + 1;
        if (digit == 11) {
            setIdError(undefined);
        } else {
            setIdError("Id number must be 11 digits");
        }
        setIdNumber(event.target.value);
        setError(undefined)
    }

    let html = "";
    if (isOwner) {
        html = (
        <div className='container card' style={{ background: '#f3f5f5' }}>
            <h2 className='text-center card-header'>ID Verification</h2>
            <div className='card-body'>

                <img className='img-thumbnail' width="400" height="400" src={newImage} alt='...' />

                <Input label='Add ID photo' type="file" onChange={onChangeFile} error={imageError} />
                <hr />

                <Input label='ID number' type="text" onChange={onChangeIdNumber} error={idError} />

                {error && <div className="alert alert-danger" role="alert">
                    {error}
                </div>}
                <ButtonWithProgress onClick={saveId} text='Upload ID' disabled={idError} />
            </div>
        </div>
        )
    }else{
        props.history.push('/');
    }


    return (
        <div>
            {html}
        </div>
    );
};

export default VerifyAccount;