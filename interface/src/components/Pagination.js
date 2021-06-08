import React from 'react';


const Pagination = ({ changepage, page_now, num_total_curr_posts, num_posts_inpage }) => {

  const pagescount = [];

  for (let item = 1; item <= Math.ceil(num_total_curr_posts / num_posts_inpage); item++) {
    pagescount.push(item);
  }

  return (

      <ul className='pagination' style={{margin: '0', marginTop:'30%', marginLeft: '50%'}}>
          <li className="page-item">

              <button className="page-link" onClick={() => changepage(page_now - 1)} >
                  
                  <span aria-hidden="true">Previous</span>
                
              </button>
          </li>
        {pagescount.map(i => (
          <li key={i} className='page-item'>
            <button onClick={() => changepage(i)}   value={i} className='page-link pgnum'>
              {i}
            </button>
          </li>
        ))}
          <li className="page-item">

              <button className="page-link" onClick={() => changepage(page_now + 1)} >
                  
                  <span aria-hidden="true">Next</span>
                  
              </button>
          </li>
      </ul>

  );
};

export default Pagination;