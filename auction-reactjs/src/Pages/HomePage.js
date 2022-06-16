import React, { useEffect, useState } from 'react';
import { getAuctions, getCategories, getByCategory } from '../Api/ApiCalls';
import Product from '../Components/Product';
import './homepage.css';
import Spinner from '../Components/Spinner';
import { useApiProgress } from '../Shared/ApiProgress';
import { Link } from 'react-router-dom';


const HomePage = () => {

  const [page, setPage] = useState({
    content: []
  });
  const [category, setCategory] = useState([]);
  const [loadFailure, setLoadFailure] = useState(false);
  const { content: auction, last, first } = page;


  useEffect(() => {
    loadAuctions();
  }, []);


  const categories = async () => {
    const response = await getCategories();
    setCategory(response.data);
  }

  const loadAuctions = async page => {
    try {
      categories();
      const response = await getAuctions(page);
      setPage(response.data);
    } catch (error) {
      setLoadFailure(true);
    }
  };

  const onClickNext = () => {
    const nextPage = page.number + 1;
    loadAuctions(nextPage);
  }
  const onClickPrevious = () => {
    const previousPage = page.number - 1;
    loadAuctions(previousPage);
  }

  const pendingApiCall = useApiProgress('get', '/auction?page');


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

  if (pendingApiCall) {
    actionDiv = <Spinner />
  }

 

  return (
    <div className='body'>
      <header className="bg-dark py-4">
        <div className="container px-4 px-lg-5 my-4">
          <div className="text-center text-white">
            <h1 className="display-4 fw-bolder">Auction Market</h1>
            <p className="lead fw-normal text-white-50 mb-0">Bid to current auctions</p>
          </div>
        </div>
      </header>

      <div className='row'>

        <ul className="list-group col-12 col-md-3 mt-5 ml-5 pr-5">
          <h5 className='text-center'>Categories</h5>
          <div style={{ borderTop: "1px solid #000000 ", marginLeft: 20, marginRight: 20 }}></div>
          <Link id="category" className="list-group-item" to='/' > Homepage </Link>
          {category.map(item => {
            return <Link id="category" key={item.id} className="list-group-item" to={`/category/auction/${item.categoryName}`}> {item.categoryName} </Link>
          })}
          <br></br>

        </ul>

        <div className="container px-4 px-lg-5 mt-5">
          <div className="row gx-4 gx-lg-5 row-cols-sm-1  row-cols-md-2 row-cols-xl-3 justify-content-center">

            {pendingApiCall ? <Spinner /> : auction.map(auction => <Product key={auction.id} auction={auction} />)}

            {loadFailure && <div className='text-center text-danger'> Failed to load auctions </div>}


          </div>
          {actionDiv}
        </div>
      </div>
      <footer id="footer" className="py-5 bg-dark bottom-0">
        <div className="container"><p className="m-0 text-center text-white">Copyright &copy; Barış Çevik 2022</p></div>
      </footer>

    </div>

  );
};

export default HomePage;