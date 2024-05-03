console.log(5 + 6);

function deleteContact(cId) {
	swal({
		title: "Are you sure?",
		text: "Once deleted, you will not be able to recover this imaginary file!",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {
			if (willDelete) {
				window.location = "/user/delete/" + cId;
			} else {
				swal("Your imaginary file is safe!");
			}
		});
}

const search = () => {
	//console.log("searching...");
	let query = $("#search-input").val();

	if (query == '') {
		$(".search-result").hide();
	} else {
		console.log(query);
		//sending request to server
		let url = `http://localhost:8080/search/${query}`;
		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			//data...

			let text = `<div class='list-group'>`;
			data.forEach((contact) => {
				text += `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-action'> ${contact.name}</a>`;
			});
			text += `</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
		});

	}
}