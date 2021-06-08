import React, {Component} from 'react'
import Route from "react-router-dom/es/Route";
import {Link} from 'react-router-dom';
import {Button} from 'react-bootstrap'
//import axios from '../axios-instance';
import './Logic.css'
import Output from "./Output";

class Logic extends Component {  
    constructor(props) {
        super(props);
        this.state = {
            input_query: '',
           // gets_sugg: []   
        }
    }


    input_handle = e => {
     this.setState({
         input_query: e.target.value
     });


    }
    // all_getters_suggs = (input_query) => {
    //     this.setState(() => ({input_query: input_query, gets_sugg: []}));
    // }

    /*display_suggs() {
        const {gets_sugg} = this.state
        if (gets_sugg.length === 0)   return null           
        return (  <ul> {  gets_sugg.map(it => <li onClick={() => this.all_getters_suggs(it)}>{it}</li>)  } </ul> )
    }
*/
  /*  in_gett = async (e) => {       
        let input_query = e.target.value;
        const resp = await axios(`/results?query=${input_query}`); ///????????????????????///////
        const gets_sugg = await resp.data;
        this.setState(() => ({gets_sugg, input_query: input_query}));
       
     }*/ 

    render() {

      return (   
            <div className="in1">  
                <form onSubmit= {e => {e.preventDefault();}}>
                <div>
                    <input onChange={this.input_handle}
                       value={this.state.input_query} type="text"  id="text_in"
                           placeholder="Type something to search" className="form-control form-control-sm ml-3 w-200"/> 
                </div>
               
                    <div className={"buttons"}>
                        <Link to={{
                            pathname: "/FinalOutput",
                            state: { input_query: this.state?.input_query, }
                        }}
                          ><Button type={"submit"} className="B1">Search..</Button>
                         </Link>
                        <Route path="/FinalOutput" render={props => (
                            <Output {...props}/> )}/>
                         </div>

               
                </form>
            </div>
        )

    }
}

export default Logic