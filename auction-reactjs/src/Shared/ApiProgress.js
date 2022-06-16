import { useState, useEffect } from 'react';
import axios from 'axios';

//`/api/1.0/users/${username}/hoaxes?page=` : '/api/1.0/hoaxes?page=';

export const useApiProgress = (apiMethod, apiPath, strictPath) => {
  const [pendingApiCall, setPendingApiCall] = useState(false);

  useEffect(() => {
    let requestInterceptor, responseInterceptor;

    const updateApiCallFor = (method, url, inProgress) => {
      if (method !== apiMethod) {
        return;
      }
      if (strictPath && url === apiPath) {
        setPendingApiCall(inProgress);
      } else if (!strictPath && url.startsWith(apiPath)) {
        setPendingApiCall(inProgress);
      }
    };

    const registerInterceptors = () => {
      requestInterceptor = axios.interceptors.request.use(request => {

        //url: "/api/1.0/hoaxes?page=0"
        //url: "/api/1.0/users/user1"
        //method: "get"
        const { url, method } = request;
        updateApiCallFor(method, url, true);
        return request;
      });

      responseInterceptor = axios.interceptors.response.use(
        response => {
          const { url, method } = response.config;
          updateApiCallFor(method, url, false);
          return response;
        },

        error => {
          const { url, method } = error.config;
          updateApiCallFor(method, url, false);
          throw error;
        }
      );
    };

    const unregisterInterceptors = () => { //kaldırma işlemi
      axios.interceptors.request.eject(requestInterceptor);
      axios.interceptors.response.eject(responseInterceptor);
    };

    registerInterceptors();

    return function unmount() {
      unregisterInterceptors();
    };
  }, [apiPath, apiMethod, strictPath]);


  return pendingApiCall;
};