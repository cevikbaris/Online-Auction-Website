import { applyMiddleware, createStore, compose } from 'redux';
import authReducer from './Reducer';
import SecureLS from 'secure-ls';
import thunk from 'redux-thunk';
import { setAuthorizationHeader } from '../Api/ApiCalls';

const securels = new SecureLS();
//securels automatically encrypts the data and convert json

const getStateFromStorage = () => { //storage'dan state çek

    const localUser = securels.get('local-user');//get with key

    let stateInLocalStorage = { //storage'dan gelen state boşsa bunu dön
        isLoggedIn: false,
        name:undefined,
        username: undefined,
        email: undefined,
        image: undefined,
        password: undefined,
        roles:[],
        isApproved:false
    };

    if (localUser) { //storage'dan gelen state varsa
        return localUser;
    }

    return stateInLocalStorage;
}

const updateStateInStorage = newState => {
    securels.set('local-user', newState); //key value
}

const configureStore = () => { //ilk başlangıc değerlerimiz. loggedInState ile birlikte kullanıcı giriş yapmış şekilde başlar
   
    const initialState=getStateFromStorage();
    setAuthorizationHeader(initialState);
    const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
    const store = createStore(authReducer, getStateFromStorage(),composeEnhancers(applyMiddleware(thunk) )); 
    //thunk  authActionsda fonksiyon return ettiğimiz için lazım
   
    store.subscribe(() => { 
        updateStateInStorage(store.getState());
        setAuthorizationHeader(store.getState());
    });
    return store;
}

export default configureStore;