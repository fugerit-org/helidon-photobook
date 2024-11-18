import React, {Component, Fragment} from 'react';

class Source extends Component {

	render() {
		return <Fragment>
			<strong>Repository github</strong>
			<div style={{margin: '20px'}}>
				<p>Questa &egrave; solo una applicazione dimostrativa, trovate il codice sorgente al seguente link :
					<br/><a
						href="https://github.com/fugerit-org/quarkus-photobook">https://github.com/fugerit-org/quarkus-photobook</a>
				</p>
			</div>
			<strong>Stack applicativo</strong>
			<table align={"center"}>
				<thead>
				<tr>
					<th>Layer</th>
					<th>2024 version</th>
				</tr>
				</thead>
				<tbody>
				<tr>
					<td>Persistence</td>
					<td>MongoDB 8</td>
				</tr>
				<tr>
					<td>Java version</td>
					<td>GraalVM 21</td>
				</tr>
				<tr>
					<td>API REST</td>
					<td>Helidon 4.1.x</td>
				</tr>
				<tr>
					<td>Node JS</td>
					<td>Node 20</td>
				</tr>
				<tr>
					<td>Front end package</td>
					<td>Vite</td>
				</tr>
				<tr>
					<td>Front end UI</td>
					<td>React 18.3</td>
				</tr>
				</tbody>
			</table>
		</Fragment>
	}

}

export default Source;