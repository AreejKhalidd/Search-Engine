import React,{Component } from 'react';
import { BrowserRouter, Route, Switch} from 'react-router-dom'
import Home from './components/Home'
import Output from './components/Output'


class App extends Component {   
  render(){
    return (
      <BrowserRouter>
      <div className="App">
        <Switch>
        <Route exact path='/' render={(props) => <Home {...props}/>}/>
        <Route  path="/finalOutput" component={Output} />
        </Switch>
      </div>
      </BrowserRouter>
    );
  }
  }
  

export default App;
