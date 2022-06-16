import React, { useState, useRef, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { logoutSuccess } from '../Redux/Actions';
import ProfileImageWithDefault from './ProfileImageWithDefault';


const Topbar = () => {

    const { username, isLoggedIn, name, image, roles } = useSelector(store => ({
        username: store.username,
        isLoggedIn: store.isLoggedIn,
        name: store.name,
        image: store.image,
        roles: store.roles
    }))
    const isAdmin = roles.some(role => role.name === 'ROLE_ADMIN');

    const menuArea = useRef(null);

    const [menuVisible, setMenuVisible] = useState(false);

    const dispatch = useDispatch();

    useEffect(() => {
        document.addEventListener('click', menuClickTracker);
        return () => {
            document.removeEventListener('click', menuClickTracker);
        };
    }, [isLoggedIn]);//nereye tıkladığımıza bakıyor

    const menuClickTracker = event => { //menüyü temsil ediyor meniye koyduk çünkü htmlde
        if (menuArea.current === null || !menuArea.current.contains(event.target)) {
            setMenuVisible(false);
        }
    };


    const onLogoutSuccess = () => {
        dispatch(logoutSuccess());
    }


    let links = (<ul className='navbar-nav ml-auto'>
        <li>
            <Link className='nav-link' to='/login' >
                Login
            </Link>
        </li>
        <li className='px-4'>
            <Link className='nav-link' to='/signup'>
                Signup
            </Link>
            {/* <Link className='nav-link' to='/ws'>
                chat
            </Link> */}
        </li>
    </ul>
    );

    if (isLoggedIn) {
        let dropDownClass = 'dropdown-menu p-0 shadow';
        if (menuVisible) {
            dropDownClass += ' show';
        }
        let profileButton = 'navbar-nav ml-auto';
        if (isAdmin) {
            profileButton = 'navbar-nav ml-2';
        }

        links = (
            <ul className={profileButton} ref={menuArea}>
                <li className='nav-item dropdown'>

                    <div className='d-flex' style={{ cursor: 'pointer' }}
                        onClick={() => setMenuVisible(true)}>
                        <ProfileImageWithDefault image={image}
                            width="32"
                            height="32" className="rounded-circle my-1" />
                        <span className='nav-link dropdown-toggle'>{name}</span>
                    </div>

                    <div className={dropDownClass}>
                        <Link className="dropdown-item d-flex p-2" to={`/user/${username}`}
                            onClick={() => setMenuVisible(false)}>
                            <i className="material-icons text-info mr-2">person</i>
                            My Profile
                        </Link>
                        <Link className="dropdown-item d-flex p-2" to={'/addproduct'}>
                            <i className="material-icons text-info mr-2">add_box</i>
                            Add New Product
                        </Link>

                        <Link className="dropdown-item d-flex p-2" to={'/my-bids'}>
                            <span className="material-icons">done</span>
                            My Bids
                        </Link>

                        <Link to={'/'}>
                            <span className="dropdown-item  d-flex p-2" onClick={onLogoutSuccess} style={{ cursor: 'pointer' }}>
                                <i className="material-icons text-danger mr-2">power_settings_new</i>
                                Logout
                            </span>
                        </Link>
                    </div>

                </li>
            </ul>

        )
    }


    return (
        <div >

            <div className=' shadow-sm bg-light mb-2 d-flex' >
                <nav className='navbar navbar-light bg-light container navbar-expand' >
                    <Link className='navbar-brand ' to='/' >
                        <img src="https://thumbs.dreamstime.com/b/auction-banner-template-ribbon-label-sign-177647990.jpg"
                             width="144" height="90"className="d-inline-block align-top" alt='brand-img' />
                       
                    </Link>

                    {isAdmin &&
                        <div className='nav-item ml-auto mr-0'>
                            <Link className='nav-link' to='/admin'>
                                <button type="button" className="btn btn-secondary"> Admin Panel </button>
                            </Link>
                        </div>}
                    {links}

                </nav>
            </div>

        </div >
    );
};

export default Topbar;