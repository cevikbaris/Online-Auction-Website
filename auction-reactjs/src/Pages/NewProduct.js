import React, { useEffect } from 'react';
import Input from '../Components/Input';
import ButtonWithProgress from '../Components/ButtonWithProgress';
import { useState } from 'react';
import { createAuction } from '../Api/ApiCalls';
import { postAuctionAttachment } from '../Api/ApiCalls';
import { useApiProgress } from '../Shared/ApiProgress';
import { useSelector } from 'react-redux';
import { getCategories } from '../Api/ApiCalls';
import { Link } from 'react-router-dom';

const NewProduct = (props) => {
    const [newImage, setNewImage] = useState(null);
    const [errors, setError] = useState({});
    const [imageId, setImageId] = useState();
    const [form, setForm] = useState({
        title: null,
        startPrice: null,
        sellNowPrice: null,
        startDate: null,
        endDate: null,
        fileAttachment: null,
        explanation: null,
        category: null,
        minimumIncrease: null
    });
    const [category, setCategory] = useState([]);
    const { username: loggedInUsername, isApproved } = useSelector(store => ({ username: store.username, isApproved: store.isApproved }))
    const pendingApiCall = useApiProgress('post', '/auction');

    useEffect(() => {
        categories();
    }, [])

    const categories = async () => {
        const response = await getCategories();
        setCategory(response.data);
    }


    const onChange = (event) => {
        const { name, value } = event.target;


        setError((previousError) => ({ ...previousError, [name]: undefined }));

        setForm((previousForm) => ({ ...previousForm, [name]: value }));
    }


    const onChangeFile = (event) => {
        if (event.target.files.length < 1) {
            return;
        }
        const file = event.target.files[0];
        const fileReader = new FileReader();
        fileReader.onload = () => {
            setNewImage(fileReader.result);
            uploadFile(file);
        }
        fileReader.readAsDataURL(file);
    }
    const uploadFile = async file => {
        const attachment = new FormData();
        attachment.append('file', file);
        const response = await postAuctionAttachment(attachment);
        setImageId(response.data.id);
    };



    const onClickSave = async (event) => {
        event.preventDefault();
        const { title, startPrice, sellNowPrice, endDate, explanation ,minimumIncrease} = form;
        let timeNow = new Date();
        let timeEnd = new Date(endDate);
        timeEnd = timeEnd.toISOString();
        let myCategory = document.getElementById('category');
        var selectedValue = myCategory.options[myCategory.selectedIndex].value;

        const numberRegex = /^[0-9]+$/;

        if (title === null || startPrice === null || sellNowPrice === null || endDate === null ||
                                                 explanation === "" || minimumIncrease === null) {
            setError({
                title: title === null ? 'Title is required' : undefined,
                startPrice: startPrice === null ? 'Start price is required' : undefined,
                sellNowPrice: sellNowPrice === null ? 'Sell now price is required' : undefined,
                endDate: endDate === null ? 'End date is required' : undefined,
                explanation: explanation === null ? 'Explanation is required' : undefined,
                minimumIncrease: minimumIncrease === null ? 'Minimum increase is required' : undefined
            });
            return;
        }
        if (!startPrice.match(numberRegex) || !sellNowPrice.match(numberRegex) || !minimumIncrease.match(numberRegex)) {
            setError({
                startPrice: !startPrice.match(numberRegex) ? 'Start price must be number' : undefined,
                sellNowPrice: !sellNowPrice.match(numberRegex) ? 'Sell now price must be number' : undefined,
                minimumIncrease: !minimumIncrease.match(numberRegex) ? 'Minimum increase must be number' : undefined
            });
            return;
        }
        const body = {
            title,
            startPrice,
            sellNowPrice,
            endDate: timeEnd.slice(0, 19),
            startDate: timeNow.toISOString().slice(0, 19),
            attachmentId: imageId,
            explanation,
            category: selectedValue,
            minimumIncrease
        };
        try {
            const response = await createAuction(loggedInUsername, body);
            props.history.push(`/auction/${response.data}`);
        } catch (error) {
            if (error.response.data.validationErrors) {
                setError(error.response.data.validationErrors);
            }
        }
    }

    let tesxtAreaClass = 'form-control';
    if (errors.explanation) {
        tesxtAreaClass += ' is-invalid';
    }


    const { title: titleError, startPrice: startPriceError, endDate: endDateError,
        sellNowPrice: sellNowPriceError, explanation: explanationError,minimumIncrease:minimumIncreaseError } = errors;

    return (
        <>
            {isApproved ?
                <div className='container border border-secondary rounded py-2 my-5 '
                    style={{ background: '#F7F7F9' }}>
                    <div className='col-xl-9 col-lg-10 col-md-12 col-sm-12 mx-auto'>
                        <h1 className='text-center'> Sell Your Product </h1>

                        <img className='img-thumbnail'
                            width="400" height="400" src={newImage} alt='...' />

                        <Input label='Add photo for product' type="file" onChange={onChangeFile} />
                        <Input label='Title' name='title' onChange={onChange} error={titleError} />

                        <p>Select Category</p>
                        <select name='category' id='category'>
                            {category.map(item => {
                                return <option key={item.id} value={item.id}>{item.categoryName}</option>
                            })}

                        </select >
                        <br />
                        <p>Explanation</p>
                        <textarea
                            className={tesxtAreaClass}
                            rows="3"
                            name='explanation'
                            onChange={onChange} />
                        <div className='invalid-feedback'>{explanationError}</div>

                        <br />


                        <Input label='Starting Price' name='startPrice' onChange={onChange} error={startPriceError} />
                        <Input label='Minimum bid increasing' name='minimumIncrease' onChange={onChange} error={minimumIncreaseError} />
                        <Input label='Sell Now Price' name='sellNowPrice' onChange={onChange} error={sellNowPriceError} />
                        <Input label='End Date' name='endDate' type='datetime-local' onChange={onChange} error={endDateError} />

                        <div className='text-center'>
                            <ButtonWithProgress text='Upload the Product' onClick={onClickSave}
                                pendingApiCall={pendingApiCall} />

                        </div>
                    </div>
                </div>

                : (<>
                    <h4 className='text-center mt-4 text-danger'>You have to authenticate your account..</h4>
                    <br />
                    <div className='text-center'>
                        <Link className="btn btn-success my-4" to={`/verify/${loggedInUsername}`}> Verify your account </Link>
                    </div>
                </>)}
        </>
    );
};

export default NewProduct;