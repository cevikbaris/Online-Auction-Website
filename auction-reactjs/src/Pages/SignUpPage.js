import React, { useState } from 'react';
import Input from '../Components/Input';
import ButtonWithProgress from '../Components/ButtonWithProgress';
import { useApiProgress } from '../Shared/ApiProgress';
import { signupHandler } from '../Redux/Actions';
import { useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';
import './loginPage.css'

const SignUpPage = (props) => {

    const [form, setForm] = useState({
        name: null,
        username: null,
        email: null,
        password: null,
        passwordRepeat: null
    });

    const [errors, setError] = useState({});
    const dispatch = useDispatch();

    const signupApiCall = useApiProgress('post', '/users', true);;
    const loginApiCall = useApiProgress('post', '/api/1.0/auth');
    const pendingApiCall = signupApiCall || loginApiCall;

    const onChange = (event) => {
        //event.target.name => name attribute of the input
        //event.target.value => value of the input
        const { name, value } = event.target;

        setError((previousError) => ({ ...previousError, [name]: undefined }));

        setForm((previousForm) => ({ ...previousForm, [name]: value }));
    }

    const onClickSignup = async (e) => {
        e.preventDefault();
        const { name, username, password, email } = form;
        const body = {
            name,
            email,
            username,
            password
        }

        const { history } = props;
        const { push } = history;

        try {
            await dispatch(signupHandler(body));
            push('/');
        } catch (error) {
            if (error.response.data.validationErrors) {
                setError(error.response.data.validationErrors);//back end den gelen validation error mapının value sunu alıcaz
            }
        }
    }

    const { password: passwordError, name: nameError, username: usernameError, email: emailError } = errors;

    let passwordRepeatError;
    if (form.password !== form.passwordRepeat) {
        passwordRepeatError = 'Password mismatch';
    }



    // <div className='container'>
    //     <form>
    //         <h1 className='text-center'>Sign Up</h1>

    //         <Input name='name' label='Name Surname' error={nameError} onChange={onChange} />

    //         <Input name='username' label='Username' error={usernameError} onChange={onChange} />

    //         <Input name='email' label='Email' error={emailError} onChange={onChange} type='email' />

    //         <Input name='password' label='Password' error={passwordError} onChange={onChange} type='password' />

    //         <Input name='passwordRepeat' label='Password Repeat' error={passwordRepeatError} onChange={onChange} type='password' />
    //         <div className='text-center'>
    //             <ButtonWithProgress
    //                 onClick={onClickSignup}
    //                 disabled={passwordRepeatError !== undefined || pendingApiCall}
    //                 pendingApiCall={pendingApiCall}
    //                 text='Sign Up'
    //             />
    //         </div>
    //     </form>
    // </div>

    return (

        <div className=" gradient-custom">
            <div className="container py-4  h-100">
                <div className="row d-flex justify-content-center align-items-center h-100">
                    <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                        <div className="card bg-dark text-white" style={{ "border-radius": "1rem" }} >
                            <div className="card-body p-5 text-center">

                                <div className="mb-md-5 mt-md-4 pb-5">

                                    <h2 className="fw-bold mb-2 text-uppercase">Sign up</h2>
                                    <p className="text-white-50 mb-5">Please enter informations!</p>

                                    <div className="form-outline form-white mb-4">
                                        <Input name='name' label='Name Surname' error={nameError} onChange={onChange} />
                                    </div>

                                    <div className="form-outline form-white mb-4">
                                        <Input name='username' label='Username' error={usernameError} onChange={onChange} />
                                    </div>

                                    <div className="form-outline form-white mb-4">
                                        <Input name='email' label='Email' error={emailError} onChange={onChange} type='email' />
                                    </div>

                                    <div className="form-outline form-white mb-4">
                                        <Input name='password' label='Password' error={passwordError} onChange={onChange} type='password' />
                                    </div>

                                    <div className="form-outline form-white mb-4">
                                        <Input name='passwordRepeat' label='Password Repeat' error={passwordRepeatError} onChange={onChange} type='password' />

                                    </div>


                                    <ButtonWithProgress
                                        onClick={onClickSignup}
                                        disabled={passwordRepeatError !== undefined || pendingApiCall}
                                        pendingApiCall={pendingApiCall}
                                        text='Sign Up'
                                    />

                                </div>

                                <div>
                                    <p className="mb-0">Do you have account? <Link to='/login' className="text-white-50 fw-bold">Login</Link>
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

export default SignUpPage;