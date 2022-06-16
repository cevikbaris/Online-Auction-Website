import * as ACTIONS from './Constants'

let defaultState = { 
    isLoggedIn: false,
    name:undefined,
    username: undefined,
    email: undefined,
    image: undefined,
    password: undefined,
    roles:[],
    isApproved:false
};
//actions dan gönderilen payloadı ve defaultState'i birleştiriyoruz.
// buna göre yeni bir state oluşturuyoruz. dönüyoruz.

const authReducer = (state={...defaultState}, action) => {//action dispatch oldugunda buraya gelir
  
   if (action.type === ACTIONS.LOGOUT_SUCCESS) {
      return defaultState;
   }
   else if (action.type === ACTIONS.LOGIN_SUCCESS) {
      
      return {
         ...action.payload,
         isLoggedIn: true,
         isApproved:action.payload.approved
      }
   }
   
   else if(action.type === ACTIONS.UPDATE_SUCCESS){
      return {
         ...state,
         name: action.payload.name,
         image: action.payload.image
      }
   }

   
   return state;
}

export default authReducer;