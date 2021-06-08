import React from 'react'
import {useState, useEffect} from 'react'
import Pagination from './Pagination'
import  axios  from '../axios-instance'
import './Output.css'
import FinalOutput from './FinalOutput'

const Output = props =>{


        const [is_loading,set_istLoading] = useState(false);
        const [is_found,set_found] = useState(false);
        const [num_posts_inpage] = useState(10);
        const [curr_posts,set_curr_posts] = useState([]);   
        const[page_now,set_page_now]=useState(1);
        const position_lastpost = page_now * num_posts_inpage;
        const position_firstpost = position_lastpost - num_posts_inpage;
        const post = curr_posts.slice(position_firstpost, position_lastpost);       
        const changepage = num_ofpage => set_page_now(num_ofpage); //yhawel el page now lel page el b2a feeha now
        if(props.location.state) { localStorage.setItem('input_query',  props.location.state.input_query); }
        const input_query = localStorage.getItem('input_query');


    let inputs = {
        input_query: input_query,
    }
    //let inputs2 = JSON.stringify(inputs);
    useEffect(() => {
        const fetchPosts =  () =>{   
            set_istLoading(true);
            axios.get('http://localhost:4000/results?'+input_query).then(out => {
                console.log(out);
                console.log(input_query);
                if(out.data==""){
                    console.log("NO RESULTS FOUND");
                    set_found(true);
                    return;
                }
                else{
                    set_curr_posts(out.data.results);
                    set_istLoading(false); //not loading
            }
                }).catch(error => {
                    console.error(error);
                });
        };
        fetchPosts();
    }, []);



    return(
        <div>
            <h1>The Result of your search .. </h1>
            <hr  style={{
            color: "purple",
            height: 5
        }}></hr>
       
            <FinalOutput  is_loading={is_loading} is_found={is_found} final_posts={post} />
            <Pagination style={{float: 'bottom',marginBottom: '3%'}}
                            changepage = {changepage}
                            page_now = {page_now}
                            num_total_curr_posts = {curr_posts.length}
                            num_posts_inpage = {num_posts_inpage}
                                />
      </div>

    )
}
export default Output