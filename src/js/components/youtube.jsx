import React from 'react';

export function YoutubeVideo({videoId}) {
  const src = "https://www.youtube.com/embed/" + videoId;

  return (
    <iframe
      width="560"
      height="315"
      src={src}
      frameBorder="0"
      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
      allowFullScreen/>
  )
}
