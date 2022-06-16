import React from 'react';
import HomePage from '../Pages/HomePage';
import { HashRouter, BrowserRouter, BrowserRouter as Router, Route, Switch, Redirect } from "react-router-dom";
import UserSignUp from '../Pages/SignUpPage';
import Topbar from '../Components/Topbar';
import LoginPage from '../Pages/LoginPage';
import {useSelector} from 'react-redux';
import UserPage from '../Pages/UserPage';
import AdminPage from '../Pages/AdminPage';
import NewProduct from '../Pages/NewProduct';
import Auction from '../Pages/Auction';
import MyBids from '../Pages/MyBids';
import ChatRoom from '../Components/ChatRoom';
import Categories from '../Components/Categories';
import VerifyAccount from '../Components/VerifyAccount';
import UserAuth from '../Components/UserAuth';

const App = () => {

    const {isLoggedIn} = useSelector(store => ({ isLoggedIn: store.isLoggedIn }));
    const roles = useSelector(store => (store.roles));
    const isAdmin = roles.some(role=>role.name === 'ROLE_ADMIN');
    return (
        <div>
            <HashRouter>
                {<Topbar />}
                <Switch>  {/* sadece bir route calissin diye */}
                    <Route exact path="/" component={HomePage} />
                    <Route path="/signup" component={UserSignUp} />
                    {!isLoggedIn && (<Route path="/login" component={LoginPage} />)}
                    <Route path="/user/:username" component={UserPage} />
                    {isAdmin && <Route path="/admin" component={AdminPage} />}
                    {isLoggedIn && (<Route path="/addproduct" component={NewProduct} />)}
                    <Route path="/auction/:id" component={Auction} />
                    <Route path="/my-bids" component={MyBids}/>
                    <Route path="/ws" component={ChatRoom} />
                    <Route path="/category/auction/:categoryName"component={Categories} />
                    {isLoggedIn && <Route path="/verify/:username" component={VerifyAccount} />}
                    {isAdmin && <Route path="/identity/:idNumber" component={UserAuth} />}
                    <Redirect to="/" />
                </Switch>
            </HashRouter>
        </div>
    );
};

export default App;