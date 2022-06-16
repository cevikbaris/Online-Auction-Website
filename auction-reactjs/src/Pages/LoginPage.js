import React, { useEffect, useState } from 'react';
import Input from '../Components/Input';
import ButtonWithProgress from '../Components/ButtonWithProgress';
import { useApiProgress } from '../Shared/ApiProgress';
import { useDispatch } from 'react-redux';
import { loginHandler } from '../Redux/Actions';
import { useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import './loginPage.css'

const LoginPage = (props) => {

    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [error, setError] = useState();
    const dispatch = useDispatch();

    useEffect(() => {
        setError(undefined)
    }, [username, password])

    const onClickLogin = async (event) => {
        event.preventDefault();

        const creds = { username, password };

        const { history } = props;
        const { push } = history;
        setError(undefined);
        try {
            const response = await dispatch(loginHandler(creds));
            const roles = response.data.role;
            if (roles.length > 1) {
                push('/admin');
            } else {
                push('/');
            }


        } catch (error) {
            if (error.response.data.message === "Unauthorized") {
                setError("Username or password is incorrect");
            } else {
                setError(error.response.data.message)
            }
        }
    }


    const buttonEnabled = username && password;
    const pendingApiCall = useApiProgress('post', '/auth', true);


    return (
        <div className=" gradient-custom">
            <div className="container py-5 h-100">
                <div className="row d-flex justify-content-center align-items-center h-100">
                    <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                        <div className="card bg-dark text-white" style={{ "border-radius": "1rem" }} >
                            <div className="card-body p-5 text-center">

                                <div className="mb-md-5 mt-md-4 pb-5">

                                    <h2 className="fw-bold mb-2 text-uppercase">Login</h2>
                                    <p className="text-white-50 mb-5">Please enter your Username and password!</p>

                                    <div className="form-outline form-white mb-4">
                                        <Input label='Username' onChange={event => setUsername(event.target.value)} />
                                    </div>

                                    <div className="form-outline form-white mb-4">
                                        <Input label='Password' type='password' onChange={event => setPassword(event.target.value)} />
                                    </div>
                                    {error && <div className='alert alert-danger'>{error}</div>}

                                    <ButtonWithProgress
                                        onClick={onClickLogin}
                                        disabled={!buttonEnabled || pendingApiCall}
                                        pendingApiCall={pendingApiCall}
                                        text={'Login'} />

                                </div>

                                <div>
                                    <p className="mb-0">Don't have an account? <Link to='/signup' className="text-white-50 fw-bold">Sign Up</Link>
                                    </p>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default LoginPage;