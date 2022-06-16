import * as ACTIONS from './Constants'
import { login, signup } from '../Api/ApiCalls';

export const logoutSuccess = () => {
    return {
        type: ACTIONS.LOGOUT_SUCCESS
    }
};

export const loginSuccess = (authState) => {
    return {
        type: ACTIONS.LOGIN_SUCCESS,
        payload: authState
    }
}

export const loginHandler = (creds) => {
    return async function (dispatch) {
        const response = await login(creds);  //api process
       
        const authState = {
            //basic auth
            ...response.data,//username,fullname,image,email
            password: creds.password,//response de gelmiyor
            roles: response.data.role
        }
        dispatch(loginSuccess(authState));
        return response;
    }
}

export const signupHandler = (user) => {
    return async function (dispatch) {
        const response = await signup(user);  //api process

        
        await dispatch(loginHandler(user))
        return response;
    }
}

export const updateSuccess = ({ name, image }) => {
    return {
        type: ACTIONS.UPDATE_SUCCESS,
        payload: { name, image }
    }
}

