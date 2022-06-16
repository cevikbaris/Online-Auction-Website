import React from 'react';
import { myWonAuctions } from '../Api/ApiCalls';
import { useSelector } from 'react-redux';
import Product  from '../Components/Product';
import { useState } from 'react';
import { useEffect } from 'react';

const MyBids = () => {

    const { loggedInUsername } = useSelector(store => ({ loggedInUsername: store.username }));
    const [loadFailure, setLoadFailure] = useState(false);
    const [auction , setAuction] = useState([]);    
    useEffect(() => {
        loadAuctions();
    }, []);

    const loadAuctions = async() => {
        try {
            const response =await myWonAuctions(loggedInUsername);
            setAuction(response.data);
            if(response.data===""){
                setLoadFailure(true);
            }
        } catch (error) {
            setLoadFailure(true);
        }
    };
    

    return (
        <div>


            
            <div className="container px-4 px-lg-5 mt-5">
               {!loadFailure && <h2 className='text-center text-success'> Auctions you have won </h2>}
                <div className="row gx-4 gx-lg-5 row-cols-sm-1  row-cols-md-3 row-cols-xl-3 justify-content-center">

                    {!loadFailure && auction.map(auction => (
                        <Product key={auction.id} auction={auction} />
                    ))}

                    {loadFailure && <h3><div className='text-center text-danger'> You have not won any auction.</div></h3> }


                </div>
             
            </div>


        </div>
    );
};

export default MyBids;