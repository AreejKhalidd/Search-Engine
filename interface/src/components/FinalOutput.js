import './Output.css'
import React from 'react';

const FinalOutput = ({is_loading,is_found, final_posts}) => {

  if (is_found==true) {
    return <h2> No results found!.... </h2>;
  }

 else if (is_loading) {
    return <h2> Still Loading.... </h2>;
  }
 

  return (
    <ul className='list-group mb-3'>
              {final_posts.map(curr => (
          <div className="pp" key = {curr.id}> <div className="card-content"  style={{marginLeft: '4%'}}>
              <a  style={{"color": "purple"}}>{curr.url}</a>
                   <br/>  <br/>
              <a href={curr.url} style={{"color": "rgb(212, 28, 89)"}} className="card-title red-text" >{curr.title}</a>
                    <br/> <br/>
               <h5 style={{"color": "black"}}>{curr.description}</h5>
               <hr
        style={{
            color: "black",
            height: 5  
        }}
    />
                     <br/> 


        </div> </div> ))}
    </ul>
  );
};

export default FinalOutput;