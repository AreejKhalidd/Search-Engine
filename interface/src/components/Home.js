import React, {Component} from "react";
import Logic from "./Logic";
import Search from "../search.jpg"; 


class Home extends Component {

    render() {
        return (
            <div id={"logo-div"} >
                <img src={Search} id={'search-logo'} style={{
                    "position": "absolute",
                    "left": "50%",
                    "right": "50%",
                     "transform": "translate(-50%)"
                }}/>
                <Logic />
            </div>
        );
    }
}

export default Home;