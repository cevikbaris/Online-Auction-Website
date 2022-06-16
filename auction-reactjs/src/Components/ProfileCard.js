import React, { useState, useEffect } from 'react';
import ProfileImageWithDefault from './ProfileImageWithDefault';
import Input from './Input';
import { updateUser } from '../Api/ApiCalls';
import ButtonWithProgress from './ButtonWithProgress';
import { useApiProgress } from '../Shared/ApiProgress';
import { useSelector, useDispatch } from 'react-redux';
import { useParams } from 'react-router-dom';
import { updateSuccess } from '../Redux/Actions';
import Product from './Product';
import { getAuctionOfUser } from '../Api/ApiCalls';
import './profile.css';
import { Link } from 'react-router-dom';

const ProfileCard = props => {

  const [inEditMode, setInEditMode] = useState(false);
  const [updatedName, setUpdatedName] = useState();
  const [user, setUser] = useState({});
  const { username: loggedInUsername, isApproved } =
    useSelector(store => ({ username: store.username, isApproved: store.isApproved }));
  const routeParams = useParams();
  const pathUsername = routeParams.username;
  const [editable, setEditable] = useState(false);
  const [newImage, setNewImage] = useState();
  const [validationErrors, setValidationErrors] = useState({});
  const dispatch = useDispatch();
  const [loadFailure, setLoadFailure] = useState(false);


  const [page, setPage] = useState({
    content: [],
    size: 5,
    number: 0
  });

  useEffect(() => {
    setUser(props.user);
  }, [props.user]);


  useEffect(() => {
    setEditable(pathUsername === loggedInUsername)
  }, [pathUsername, loggedInUsername]);

  const { username, name, image } = user;


  useEffect(() => {
    if (!inEditMode) {
      setUpdatedName(undefined); // if yo are not in edit mode, then reset the name
      setNewImage(undefined); // if yo are not in edit mode, then reset the new image
    } else {
      setUpdatedName(name);
    }
  }, [inEditMode, name]);


  useEffect(() => {
    setValidationErrors(previousValidationErrors => ({
      ...previousValidationErrors,
      name: undefined
    }));
  }, [updatedName]);

  useEffect(() => {
    setValidationErrors(previousValidationErrors => ({
      ...previousValidationErrors,
      image: undefined
    }));
  }, [newImage]);

  //----------------------------LOAD AUCTIONS OF USER---------------------------------
  useEffect(() => {
    loadAuctions(pathUsername);
    setLoadFailure(false);
  }, [pathUsername]);


  const loadAuctions = async (username, page) => {
    try {
      const response = await getAuctionOfUser(username, page);
      setPage(response.data);
    } catch (error) {
      setLoadFailure(true);
    }
  };

  const onClickNext = () => {
    const nextPage = page.number + 1;
    loadAuctions(pathUsername, nextPage);
  }

  const onClickPrevious = () => {
    const previousPage = page.number - 1;
    loadAuctions(pathUsername, previousPage);
  }
  //----------------------------------------------------------------------------
  const { content: auction, last, first } = page;



  const onClickSave = async () => {
    let image;
    if (newImage) {
      image = newImage.split(',')[1]; // get the image data from the input
    }
    const body = {
      name: updatedName,
      image
    };
    try {
      const response = await updateUser(username, body);
      setInEditMode(false);
      setUser(response.data);
      dispatch(updateSuccess(response.data));
    } catch (error) {
      setValidationErrors(error.response.data.validationErrors);

    }
  };


  const onChangeFile = event => {
    if (event.target.files.length < 1) {
      return;
    }
    const file = event.target.files[0];
    const fileReader = new FileReader();
    fileReader.onloadend = () => {
      setNewImage(fileReader.result);
    };
    fileReader.readAsDataURL(file);
  };

  const pendingApiCall = useApiProgress('put', '/users/' + username);
  const { name: nameError, image: imageError } = validationErrors;


  let actionDiv = (
    <div className='px-0'>
      {first === false && (
        <button className="previous " onClick={onClickPrevious}>
          &laquo; Previous
        </button>
      )}
      {last === false && (
        <button className="float-right previous" onClick={onClickNext}>
          Next &raquo;
        </button>
      )}
    </div>
  );

  return (


    <div>
      <div className="card text-center">
        <div className="card-header">
          <ProfileImageWithDefault className="rounded-circle shadow"
            width="200" height="200" alt={`${username} profile`}
            image={image} tempimage={newImage}

          />
        </div>
        <div className="card-body">
          {!inEditMode && (
            <>
              <h3>
                {name}
              </h3>
              {editable &&
                (<div>
                  <button className="btn btn-success d-inline-flex" onClick={() => setInEditMode(true)}>
                    <i className="material-icons">edit</i>
                    Edit
                  </button>
                  <br/>
                  {!isApproved
                    ? <Link className="btn btn-success my-4" to={`/verify/${username}`}> Verify your account </Link>
                    : <span className='text-success'> Your account is approved</span>}
                </div>

                )}
            </>
          )}
          {inEditMode && (
            <div>
              <Input label='Change profile photo' type="file" onChange={onChangeFile} error={imageError} />


              <Input
                label='Change Display Name'
                defaultValue={name}
                onChange={event => {
                  setUpdatedName(event.target.value);
                }}
                error={nameError}
              />

              <div>
                <ButtonWithProgress
                  className="btn btn-primary d-inline-flex"
                  onClick={onClickSave}
                  disabled={pendingApiCall}
                  pendingApiCall={pendingApiCall}
                  text={
                    <>
                      <i className="material-icons">save</i>
                      Save
                    </>
                  }
                />
                <button className="btn btn-light d-inline-flex ml-1" onClick={() => setInEditMode(false)} disabled={pendingApiCall}>
                  <i className="material-icons">close</i>
                  Cancel
                </button>
              </div>
            </div>
          )}
        </div>


      </div>

      <h4 className='text-center mt-4 text-danger'>{auction.length > 0 ? `Products of ${name}` : `${name} does not have any product.`}</h4>

      <div className="container px-4 px-lg-5 mt-5">
        <div className="row gx-4 gx-lg-5 row-cols-sm-1  row-cols-md-3 row-cols-xl-3 justify-content-center">
          {auction.map(auction => (
            <Product key={auction.id} auction={auction} />
          ))}



        </div>


      </div>

    </div>


  );
};

export default ProfileCard;