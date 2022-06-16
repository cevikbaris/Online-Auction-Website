import axios from 'axios';

export const signup = (body) => {
    return axios.post('/users', body);
}

export const login = creds => {
    return axios.post('/auth', {}, { auth: creds });
    //url - data -config
}

export const getUsers = (page = 0, size = 8) => {
    return axios.get(`/users?page=${page}&size=${size}`);
}

export const getUser = (username) => {
    return axios.get(`/users/${username}`);
}

export const updateUser = (username, body) => {
    return axios.put(`/users/${username}`, body);
}

export const createAuction = (username, body) => {
    return axios.post(`/auction/${username}`, body);
}

export const getAuction = (id) => {
    return axios.get(`/auction/${id}`);
}
export const getAuctions = (page = 0, size = 6) => {
    return axios.get(`/auction?page=${page}&size=${size}`);
}

export const postAuctionAttachment = (attachment) => {
    return axios.post('/auction-attachments', attachment);
}

export const getAuctionOfUser = (username, page = 0, size = 6) => {
    return axios.get(`/auction/user/${username}?page=${page}&size=${size}`);
}

export const createBid = (body) => {
    return axios.post('/auction/bid', body);
}

export const maxBidOfAuction = (auctionId) => {
    return axios.get(`/bid/auction/${auctionId}`);
}

export const myWonAuctions = (username) => {
    return axios.get(`/auction/mybids/${username}`);
}


export const getCategories = () => {
    return axios.get('/category');
}

export const getByCategory = (category) => {
    return axios.get(`/category/auction/${category}`);
}

export const postIdImage = (image) => {
    return axios.post('/identity-attachment', image);
}

export const putIdentity = (body) => {
    return axios.put('/identity', body);
}

export const getAllIdentity = () => {
    return axios.get('/identity');
}

export const getByIdIdentity = (id) => {
    return axios.get(`/identity/${id}`);
}

export const approveUser = (id) => {
    return axios.put(`/users/update/${id}`);
}

export const deleteIdentity = (id) => {
    return axios.delete(`/identity/user/${id}`);
}   

export const buyNow = (body) => {
    return axios.put('/auction/buy-now', body);
}

export const postAutoBidding  = (body) => {
    return axios.post('/auto/bid', body);
}


export const setAuthorizationHeader = ({ username, password, isLoggedIn }) => {
    if (isLoggedIn) {
        axios.defaults.headers['Authorization'] = `Basic ${btoa(username + ':' + password)}`;
    } else {
        delete axios.defaults.headers['Authorization'];
    }
}
