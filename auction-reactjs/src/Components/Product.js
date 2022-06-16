import React from 'react';
import { Link } from 'react-router-dom';
import { useCountdown } from './Countdown';
import './Product.css';
const Product = (props) => {
    const { auction } = props;
    const { title, fileAttachment, startPrice, explanation, endDate, id } = auction;

    const [days, hours, minutes, seconds] = useCountdown(endDate);


    let showTime = "";

    if (days + hours + minutes + seconds <= 0) {
        showTime = "Auction is over";
    } else {
        showTime = `${days} days ${hours} hours ${minutes} minutes ${seconds} seconds`;

    }


    return (
        <div className="col mb-5">
            <div className="card h-100">
                <img className="card-img-top" src={fileAttachment ? `images/attachments/${fileAttachment.name}` : "https://dummyimage.com/450x300/dee2e6/6c757d.jpg" } alt="..." />
                <div className="card-body p-3">
                    <div className="text-center">
                        <h5 className="fw-bolder">{title}</h5>
                        <p className='cut-lines'>{explanation}</p>
                       <h5> Price : ${Number(startPrice).toLocaleString()} </h5><br></br>
                        <span className="text-danger">{showTime}</span>



                    </div>
                </div>
                <div className="card-footer p-4 pt-0 border-top-0 bg-transparent">
                    <div className="text-center">
                        <Link className="btn btn-outline-dark mt-auto" to={`/auction/${id}`}>View Details </Link>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default Product;