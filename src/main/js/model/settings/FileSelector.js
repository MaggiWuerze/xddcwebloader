import * as React from "react";
import {Form, InputGroup} from 'react-bootstrap';

export class FileSelector extends React.Component
{
    constructor(props)
    {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(selectorFiles)
    {
        console.debug(selectorFiles);
    }

    render ()
    {
        return (
            <InputGroup className="mb-3">
                <InputGroup.Text id="basic-addon1">Download Path</InputGroup.Text>
                {/*<Form.Control placeholder="/dir/example/" aria-label="download path" aria-describedby="basic-addon1">*/}
                <input type="file" webkitdirectory directory multiple onChange={ (e) => this.handleChange(e.target.files) } />
                {/*</Form.Control>*/}
            </InputGroup>
        )
    }
}