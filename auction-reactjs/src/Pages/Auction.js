import React, { useEffect } from 'react';
import './Auction.css'
import Input from '../Components/Input';
import { useParams } from 'react-router-dom';
import { getAuction, maxBidOfAuction, createBid, buyNow, postAutoBidding } from '../Api/ApiCalls';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useCountdown } from '../Components/Countdown';
import ButtonWithProgress from '../Components/ButtonWithProgress';
import { useSelector } from 'react-redux';
import { useApiProgress } from '../Shared/ApiProgress';
import Spinner from '../Components/Spinner';
import Modal from '../Components/Modal';



const Auction = (props) => {
    const [auction, setAuction] = useState({
        bids: [],
        name: null,
        username: null,
        buyer: null,
        category: null,
        endDate: null,
        explanation: null,
        fileAttachment: null,
        sellNowPrice: null,
        startDate: null,
        startPrice: null,
        title: null,
        minimumIncrease: null
    });
    const [bidder, setBidder] = useState({
        name: null,
        price: null,
        username: null,
    });

    const [notFound, setNotFound] = useState(false);
    const [errors, setError] = useState({});
    const [price, setPrice] = useState(null);
    const [isBidded, setIsBidded] = useState(false);
    const [modalVisible, setModalVisible] = useState(false);
    const [samePriceError, setSamePriceError] = useState(undefined);
    const [buyNowModalVisible, setBuyNowModalVisible] = useState(false);
    const [automaticModalVisible, setAutomaticModalVisible] = useState(false);
    const [maxBidLimit, setMaxBidLimit] = useState(null);

    const params = useParams();
    let { id } = params;

    const { username: loggedInUsername, isLoggedIn, isApproved } = useSelector(store => ({
        username: store.username,
        isLoggedIn: store.isLoggedIn,
        isApproved: store.isApproved
    }))

    const pendingApiCall = useApiProgress('get', `/auction/${id}`);
    const bidPendingApiCall = useApiProgress('post', '/auction/bid');
    const pendingApiCallBuyNow = useApiProgress('put', '/auction/buy-now')

    useEffect(() => {
        loadAuction();
        loadBidder();
        setNotFound(false);
    }, []);

    useEffect(() => {
        loadAuction();
    }, [bidder])

    const loadAuction = async () => {
        try {
            const response = await getAuction(id);
            setAuction(response.data);
        } catch (error) {
            setNotFound(true);
        }
    }

    const loadBidder = async () => {
        try {
            const response = await maxBidOfAuction(id);
            setBidder(response.data);
            setIsBidded(true);
        } catch (error) {
            setIsBidded(false);
        }
    }


    const { endDate, explanation, fileAttachment, sellNowPrice, startPrice, title, name, username, minimumIncrease } = auction;

    const isOwner = (username === loggedInUsername);
    const [days, hours, minutes, seconds] = useCountdown(endDate);

    const isFinished = (days + hours + minutes + seconds <= 0);


    const onClickBid = async () => {
        var time = new Date();
        const body = {
            buyerUsername: loggedInUsername,
            price: price,
            auctionID: id,
            bidTime: time
        }
        try {
            const response = await createBid(body);
            setBidder(response.data);
            setIsBidded(true);
        } catch (error) {
            console.log(error.response.data);
            if (error.response.data.status === 500) {
                setSamePriceError('You can not bid the same price');
            }
        }
        finally {
            setModalVisible(false);
        }
    }




    const onChangePrice = (event) => {
        const { name, value } = event.target;

        if (value <= startPrice || value <= bidderPrice) {
            setError((previousError) => ({ ...previousError, [name]: 'Bid price must be greater than last price' }));
        } else if (value < (startPrice + minimumIncrease)) {
            setError((previousError) => ({ ...previousError, [name]: 'Bid price must be greater than minimum bid increasing' }));
        }
        else {
            setError((previousError) => ({ ...previousError, [name]: undefined }));
            setPrice(value);

        }
    }
    const onChangeMaxLimit = (event) => {
        const { name, value } = event.target;
        if (value <= (startPrice + minimumIncrease)) {
            setError((previousError) => ({ ...previousError, [name]: 'Automatic bid limit must be higher than last price + minimum increase' }));
        } else {
            setMaxBidLimit(value);
            setError((previousError) => ({ ...previousError, [name]: undefined }));
        }
    }

    const { bid: bidError, automaticBid: automaticBidError } = errors;


    const onClickCancel = () => {
        setModalVisible(false)
    }

    const onClickBuyNow = async () => {
        var time = new Date();
        const body = {
            buyerUsername: loggedInUsername,
            price: sellNowPrice,
            auctionID: id,
            bidTime: time
        }
        try {
            const response = await buyNow(body);
            setBidder(response.data);
            setIsBidded(true);
        } catch (error) {
            console.log(error);
        } finally {
            setBuyNowModalVisible(false);
        }

    }

    const createAutoBidding = async () => {
        const body = {
            maxBidLimit,
            auctionId: id,
            username: loggedInUsername
        }
        try {
            await postAutoBidding(body);
            setAutomaticModalVisible(false);

        } catch (error) {
            console.log(error);
        }
    }

    const { name: bidderName, username: bidderUsername, price: bidderPrice } = bidder;

    let showTime = "";
    let html = "";

    if (pendingApiCall) {
        html = <Spinner />
    } else {
        if (!isFinished) {
            showTime = `${days} days ${hours} hours ${minutes} minutes ${seconds} seconds`;

            html = (<>
                <span className="text-danger">{showTime}</span>
                <br />

                {isLoggedIn &&
                    (!isOwner &&
                        (isApproved
                            ? (<div className="action my-2 form-group row">

                                <div className='col-12 row '>
                                    <div className='col-7  my-auto'>
                                        <Input labelClassName="font-weight-bold" label={`Enter your bid. (Minimum increase +$${minimumIncrease})`}
                                            name='bid' id='price' type="text" onChange={onChangePrice} error={bidError || samePriceError} />
                                    </div>
                                    <div className='col-5 my-auto'>
                                        <ButtonWithProgress className="pl-5 pr-5  py-2 pb-2 btn btn-success" pendingApiCall={bidPendingApiCall || pendingApiCall}
                                            text='BID' onClick={() => setModalVisible(true)} disabled={bidError || (price == null)} />
                                    </div>
                                </div>

                                <div className='col-12 row '>
                                    <div className='col-7  my-auto'>
                                        <Input labelClassName="font-weight-bold" label='Enter automatic bidding limit.'
                                            name='automaticBid' type="text" onChange={onChangeMaxLimit} error={automaticBidError} />
                                    </div>
                                    <div className='col-5 my-auto'>
                                        <ButtonWithProgress className="py-2 pb-2 btn btn-success"
                                            text='CREATE AUTO BID' onClick={() => setAutomaticModalVisible(true)} disabled={automaticBidError} />
                                    </div>
                                </div>

                                <div className='col-12 '>
                                    <div className='font-weight-bold'>Buy Now:  <span> ${Number(sellNowPrice).toLocaleString()}</span>
                                        <ButtonWithProgress className='pl-5 pr-5  py-2 pb-2 ml-4 btn btn-danger' text='BUY NOW '
                                            onClick={() => setBuyNowModalVisible(true)} />
                                    </div>
                                </div>
                            </div>)
                            : <Link className="btn btn-success" to={`/verify/${loggedInUsername}`} > Verify your account to bid </Link>
                        )
                    )
                }
                {!isLoggedIn && <Link to="/login" className="action my-2">Login to bid</Link>}

            </>
            )
        } else {
            html = (<span className="text-danger">"Auction is over"</span>);
        }
    }
    return (
        <div className="container my-4">
            <div className="card mt ">
                <div className="container-fliud my-5">
                    {!notFound && (<div className="wrapper row">

                        <div id="carouselExampleControls" className="carousel slide preview col-md-6" data-ride="carousel">
                            <div className="carousel-inner">
                                {fileAttachment && (
                                    <div>
                                        {fileAttachment.fileType.startsWith('image') && (
                                            <div className="carousel-item active">
                                                <img src={'images/attachments/' + fileAttachment.name}
                                                    className="d-block w-100" alt={fileAttachment.name} />
                                            </div>
                                        )}
                                        {!fileAttachment.fileType.startsWith('image')
                                            && <strong>Unknown attachment</strong>}
                                    </div>
                                )}
                            </div>
                            <a className="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
                                <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                                <span className="sr-only">Previous</span>
                            </a>
                            <a className="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
                                <span className="carousel-control-next-icon" aria-hidden="true"></span>
                                <span className="sr-only">Next</span>
                            </a>
                        </div>

                        <div className="details col-md-6 my-2">
                            <h3 className="product-title">{title}</h3>
                            <h5 > Seller : <Link to={`/user/${username}`}> {name} </Link> </h5>
                            <div><hr></hr></div>


                            <h5>Description</h5>
                            <p className="product-description">{explanation}</p>

                            <div><hr></hr></div>

                            {isBidded && <h5>Highest Bid:  <span className="text-success"> ${Number(bidderPrice).toLocaleString()}</span> -<Link to={`/user/${bidderUsername}`}>{bidderName} </Link>  </h5>}

                            {!isBidded && <h4 >Current Price: <span className="text-success">${Number(startPrice).toLocaleString()}</span></h4>}



                            {pendingApiCall ? <Spinner /> : html}

                            {isFinished && <h5>Winner !! {bidderName} </h5>}


                        </div>
                    </div>)}

                    {notFound && (<div className="alert alert-danger" role="alert">
                        <strong>Auction not found</strong>
                    </div>)}

                </div>
            </div>
            <Modal visible={modalVisible}
                onClickCancel={onClickCancel}
                onClickOk={onClickBid}
                pendingApiCall={bidPendingApiCall}
                title='Bid On Product'
                okButtonText='Make A Bid'
                message={
                    <div>
                        <div>
                            <strong>Are you sure you want to bid?
                                <br></br>
                                Your bid : {price} </strong>
                        </div>
                    </div>
                } />
            <Modal visible={buyNowModalVisible}
                onClickCancel={() => setBuyNowModalVisible(false)}
                onClickOk={onClickBuyNow}
                pendingApiCall={pendingApiCallBuyNow}
                title='Buy Now Product'
                okButtonText='Buy now'
                message={
                    <div>
                        <div>
                            <strong>Are you sure you want to buy?</strong>
                            <br></br>
                            If you buy now the auction will be closed and you will be the winner.
                            <br />
                            <strong> Buy now price : <span> ${Number(sellNowPrice).toLocaleString()}</span> </strong>
                        </div>
                    </div>
                } />
            <Modal visible={automaticModalVisible}
                onClickCancel={() => setAutomaticModalVisible(false)}
                onClickOk={createAutoBidding}
                //pendingApiCall={bidPendingApiCall}
                title='Automatic Bidding'
                okButtonText='Ok'
                message={
                    <div>
                        The system will renew your bid until  reach the limit.
                        <br />
                        Your limit : ${Number(maxBidLimit).toLocaleString()}
                    </div>
                } />
        </div>
    );
};

export default Auction;