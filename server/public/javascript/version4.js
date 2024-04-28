console.log("Running")

const ce = React.createElement 

const csrfToken = document.getElementById("csrfToken").value;
const validateRoute = document.getElementById("validateRoute").value;
const tasksRoute = document.getElementById("tasksRoute").value;
const createRoute = document.getElementById("createRoute").value;
const deleteRoute = document.getElementById("deleteRoute").value;
const addRoute = document.getElementById("addRoute").value;
const logoutRoute = document.getElementById("logoutRoute").value;

class Version4MainComponent extends React.Component{
    constructor(props){
        super(props);
        this.state = {loggedIn: false};
    }

    render() {
        if(this.state.loggedIn){
            return ce(TaskListComponent, {doLogout: () => this.setState({loggedIn: false})});
        } else{
            return ce(LoginComponent, {doLogin: () => this.setState({loggedIn: true})});
        }
    }
}

class LoginComponent extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            loginName: "", 
            loginPass: "", 
            createName: "", 
            createPass: "",
            loginMessage: "",
            createMessage: "",
        };
    }

    render() {
        return ce('div', null,
            ce('h2', null, 'Login:'),
            ce('br'),
            'Username: ',
            ce('input', {type: "text", id: "loginName", value: this.state.loginName, onChange: e => this.changeHandler(e)}),
            ce('br'),
            'Password: ',
            ce('input', {type: "password", id: "loginPass", value: this.state.loginPass, onChange: e => this.changeHandler(e)}),
            ce('br'),
            ce('button', {onClick: e => this.login(e)}, 'Login'),
            ce('span', {id: "login-message"}, this.state.loginMessage),
            ce('br'),
            ce('h2', null, 'Create User: '),
            ce('br'),
            'Username: ',
            ce('input', {type: "text", id: "createName", value: this.state.createName, onChange: e => this.changeHandler(e)}),
            ce('br'),
            'Password: ',
            ce('input', {type: "password", id: "createPass", value: this.state.createPass, onChange: e => this.changeHandler(e)}),
            ce('br'),
            ce('button', {onClick: e => this.createUser(e)}, 'Create User'),
            ce('span', {id: "create-message"}, this.state.createMessage)
        );
    }

    changeHandler(e) {
        console.log(e.target['id']);
        this.setState({[e.target['id']]: e.target.value})
    }

    login(e) {
        const username = this.state.loginName;
        const password = this.state.loginPass;
        
        fetch(validateRoute, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken},
            body: JSON.stringify({username, password})
        }).then(res => res.json()).then(data => {
            if(data){
                this.props.doLogin();
            } else{
                this.setState({loginMessage: 'Login Failed'})
            }
        })
    }

    createUser(e) {
        const username = this.state.createName;
        const password = this.state.createPass;

        fetch(createRoute, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Csrf-Token': csrfToken},
            body: JSON.stringify({username, password})
        }).then(res => res.json()).then(data => {
        if(data){
            this.props.doLogin();
        } else{
            this.setState({createMessage: "Creation Failed"});
        }
    })
    }
}

ReactDOM.render(
    ce(Version4MainComponent, null, null),
    document.getElementById('react-root')
)