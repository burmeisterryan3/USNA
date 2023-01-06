#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

#include <pthread.h>

#define MAX_THREADS 5
#define BUF_SIZE 4096

pthread_t threads[MAX_THREADS];
struct info {
  fd_set* activefds;
  int client_sock;
  struct sockaddr_in* client_saddr_in;
};

void * client_handler(void * args){
  //TODO: Complete the client handler
  //
  // HINT: Consider what arguments need to be passed to client handler
  //       to print the right info.
  //
  // Output print statements:
  //

  struct info* information = (struct info*) args;

  int n;
  char response[BUF_SIZE];           //what to send to the client

  memset(response, 0, BUF_SIZE);

  while ((n = read(information->client_sock, response, BUF_SIZE)) > 0){
    //client sent a message
    
    //null response for safety
    response[n] = '\0';
    
    //write message back to client
    write(information->client_sock, response, n);
    
    //LOG
    printf("Received From: %s:%d (%d): %s", inet_ntoa(information->client_saddr_in->sin_addr), 
             ntohs(information->client_saddr_in->sin_port), information->client_sock, response);
  }

  close(information->client_sock);
  FD_CLR(information->client_sock, information->activefds);
  //LOG
  printf("Client Closed: %s:%d (%d)\n", inet_ntoa(information->client_saddr_in->sin_addr), 
         ntohs(information->client_saddr_in->sin_port), information->client_sock);
	    
  //close client socket

  
  //deactivate this client socket
  
  free(information);
  
  return NULL;
}


int main(){
  //TODO: Complete the main function
  //
  // HINT: When you start a thread, you should detach it so that you
  //       do not have to join it later. Use: pthread_detach()
  char hostname[]="127.0.0.1";   //localhost ip address to bind to
  short port=1845;               //the port we are to bind to


  struct sockaddr_in saddr_in;  //socket interent address of server
  struct sockaddr_in client_saddr_in;  //socket interent address of client

  socklen_t saddr_len = sizeof(struct sockaddr_in); //length of address

  int client_sock, server_sock;         //socket file descriptor

  fd_set *activefds = (fd_set*) calloc(1, sizeof(fd_set)), readfds;
  
  int i;

  //set up the address information
  saddr_in.sin_family = AF_INET;
  inet_aton(hostname, &saddr_in.sin_addr);
  saddr_in.sin_port = htons(port);

  //open a socket
  if( (server_sock = socket(AF_INET, SOCK_STREAM, 0))  < 0){
    perror("socket");
    exit(1);
  }
  
  //bind the socket
  if(bind(server_sock, (struct sockaddr *) &saddr_in, saddr_len) < 0){
    perror("bind");
    exit(1);
  }

  //ready to listen, queue up to 5 pending connectinos
  if(listen(server_sock, 5)  < 0){
    perror("listen");
    exit(1);
  }


  saddr_len = sizeof(struct sockaddr_in); //length of address

  printf("server sock listening: (%d)\n",server_sock);


  //set up a select set to select from
  FD_ZERO(activefds);

  //add the server socket to the select set
  FD_SET(server_sock, activefds);

  while(1){


    //set current active file descriptors
    readfds = *activefds;

    //Perform a selct
    if( select(FD_SETSIZE, &readfds, NULL, NULL, NULL) < 0){
      perror("select");
      exit(1);
    }

    //check for activity on all file descriptors
    for(i=0; i < FD_SETSIZE; i++){
      
      //was the file descriptor i set?
      if(FD_ISSET(i, &readfds)){

	if(i == server_sock){
	  //activity on server socket, incoming connection
          
	  //accept incoming connections = NON BLOCKING
	  client_sock = accept(server_sock, (struct sockaddr *) &client_saddr_in, &saddr_len);

          printf("Connection From: %s:%d (%d)\n", inet_ntoa(client_saddr_in.sin_addr), 
		 ntohs(client_saddr_in.sin_port), client_sock);

	  FD_SET(client_sock, activefds);

	}else{

          struct info* information = (struct info*) calloc(1, sizeof(struct info));

	  //otherwise client socket sent something to us
	  client_sock = i;

	  //get the foreign address of socket
	  getpeername(client_sock, (struct sockaddr *) &client_saddr_in, &saddr_len);

          information->client_sock  = client_sock;
          information->client_saddr_in = &client_saddr_in;
          information->activefds = activefds;

          FD_CLR(client_sock, activefds);

          pthread_create(&threads[i], NULL, client_handler, information);

          pthread_detach(threads[i]);
	}

      }
	 
    }

  }
  
  
  //close the socket
  close(server_sock);



  return 0; //success
}
